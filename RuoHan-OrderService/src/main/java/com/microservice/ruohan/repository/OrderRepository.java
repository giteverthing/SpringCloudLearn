package com.microservice.ruohan.repository;

import com.microservice.ruohan.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.awt.print.Pageable;
import java.util.List;

/**
 * 基于elasticsearch的仓储
 */
public interface OrderRepository extends ElasticsearchRepository<Order, Integer> {
    List<Order> findByOrderNoLike(String keywords);
}
