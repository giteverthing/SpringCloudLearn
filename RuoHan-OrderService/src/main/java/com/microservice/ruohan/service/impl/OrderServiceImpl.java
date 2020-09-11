package com.microservice.ruohan.service.impl;

import com.microservice.ruohan.entity.Order;
import com.microservice.ruohan.repository.OrderMapper;
import com.microservice.ruohan.service.OrderService;
import com.microservice.ruohan.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, rollbackFor = {Exception.class, RuntimeException.class})
    public int insert(Order order) {
        int affectRows = 0;

//        stockService.update(order.getProductId());
        affectRows = mapper.insert(order);

        return affectRows;
    }

    public List<Order> queryAll() {
        return mapper.queryAll();
    }
}
