package com.microservice.ruohan.controller;

import com.microservice.ruohan.entity.Order;
import com.microservice.ruohan.repository.OrderRepository;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.search.MatchQuery;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.Highlighter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("es_order")
public class OrderInESController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 添加到es
     *
     * @param order
     * @return
     */
    @PostMapping("/add")
    public String add(@RequestBody Order order) {
        orderRepository.save(order);
        return "success";
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable int id) {
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            return order.get();
        }
        return new Order();
    }

    @GetMapping("")
    public List<Order> getAll() {
        Iterable<Order> iterable = orderRepository.findAll();
        List<Order> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

    @PutMapping("")
    public String updateById(@RequestBody Order order) {
        orderRepository.save(order);

        return "Success";
    }

    @DeleteMapping("{id}")
    public String deleteById(@PathVariable int id) {
        orderRepository.deleteById(id);

        return "Success";
    }

    @DeleteMapping("")
    public String deleteAll() {
        orderRepository.deleteAll();

        return "Success";
    }

    @GetMapping("/search/orderNo")
    public List<Order> searchOrderNo(String keyword) {
//        QueryBuilder matchaAllQuery = QueryBuilders.matchAllQuery();
//        SearchQuery query = new NativeSearchQueryBuilder()
//                .withQuery(QueryBuilders.matchPhraseQuery("orderNo", keyword))
////                .withTypes("")
////                .withFields("orderNo")
//                .withPageable(PageRequest.of(0, 10))
//                .build();
//
//        return orderRepository.search(query);
        return orderRepository.findByOrderNoLike(keyword);
    }

    @GetMapping("/search")
    public Page<Order> search(String keyword) {
        Pageable pageable = PageRequest.of(0, 10);
        String preTag = "<font color='#dd4b39'>";
        String postTag = "</font>";

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("orderNo", keyword))
                .withHighlightFields(new HighlightBuilder.Field("orderNo").preTags(preTag).postTags(postTag))
                .withPageable(pageable)
                .withTypes("com.microservice.ruohan.entity.Order")
                .withIndices("order")
                .build();
        AggregatedPage<Order> orders = elasticsearchTemplate.queryForPage(searchQuery, Order.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                List<Order> chunk = new ArrayList<Order>();
                for (SearchHit searchHit : searchResponse.getHits()) {
                    if (searchResponse.getHits().getHits().length <= 0) {
                        return null;
                    }
                    HighlightField orderNo = searchHit.getHighlightFields().get("orderNo");
                    Map<String, Object> sourceMap = searchHit.getSourceAsMap();
                    Order order = new Order();

                    order.setId((Integer) sourceMap.get("id"));
                    order.setBuyerId((Integer) sourceMap.get("buyerId"));
                    order.setOrderNo(orderNo.getFragments()[0].toString());
                    order.setProductId((Integer) sourceMap.get("productId"));
                    chunk.add(order);
                }
                if (chunk.size() > 0) {
                    return new AggregatedPageImpl<T>((List<T>) chunk);
                }
                return null;
            }


            public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
                return null;
            }
        });

        Page<Order> pageData = orders == null ? new PageImpl<>(new ArrayList<>()) : new PageImpl<>(orders.getContent(), pageable, orders.getTotalElements());
        return pageData;
    }
}
