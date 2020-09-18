package com.microservice.ruohan.service.impl;

import com.microservice.ruohan.annotation.CustomCacheable;
import com.microservice.ruohan.entity.Order;
import com.microservice.ruohan.repository.OrderMapper;
import com.microservice.ruohan.service.OrderService;
import com.microservice.ruohan.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper mapper;
    @Autowired
    private StockService stockService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Cacheable(value = "Orders", key = "#root.methodName+'_id:'+#id")
    public Order findById(int id) {
        return mapper.findById(id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, rollbackFor = {Exception.class, RuntimeException.class})
    public int insert(Order order) {
        int affectRows = 0;

//        stockService.update(order.getProductId());
        affectRows = mapper.insert(order);

        return affectRows;
    }

    @CustomCacheable(value = "Orders", key = "#root.methodName", expire = 600000)
    public List<Order> queryAll() {
//        List<Order> list = (List<Order>) redisTemplate.opsForValue().get("OrderList");
//        if (list == null) {
//            synchronized (this) {
//                list = (List<Order>) redisTemplate.opsForValue().get("OrderList");
//                if (list == null) {
//                    list = mapper.queryAll();
//
//                    redisTemplate.opsForValue().set("OrderList", list);
//                }
//            }
//        }
//        return list;
        return mapper.queryAll();
    }
}
