package com.microservice.ruohan.controller;

import com.microservice.ruohan.annotation.CustomLog;
import com.microservice.ruohan.entity.Order;
import com.microservice.ruohan.repository.OrderRepository;
import com.microservice.ruohan.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("")
    @CustomLog(name = "getOrderList", isSaveRequestData = true)
    public List<Order> getList(HttpServletResponse response) {
//        List<Order> list = new ArrayList<>();
//        Order order = null;
//
//        for (int i = 0; i < 10; i++) {
//            order = new Order();
//            order.setId(i);
//            order.setBuyerId(201);
//            order.setOrderNo("OrderNo" + i);
//
//            list.add(order);
//        }
        List<Order> list = orderService.queryAll();

        return list;
    }

    @GetMapping("{productId}")
    @CustomLog(name = "OrderProduct", isSaveRequestData = true)
    public String add(@PathVariable int productId) {
        Order order = new Order();
        order.setBuyerId(404);
        order.setProductId(productId);
        order.setOrderNo(UUID.randomUUID().toString());
        int affectRows = orderService.insert(order);

        if (affectRows == 0) {
            return "Error.";
        }
        return "Success.";
    }
}
