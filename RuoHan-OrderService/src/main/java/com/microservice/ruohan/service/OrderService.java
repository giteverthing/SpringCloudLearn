package com.microservice.ruohan.service;

import com.microservice.ruohan.entity.Order;

import java.util.List;

public interface OrderService {
    Order findById(int id);

    int insert(Order order);

    List<Order> queryAll();
}
