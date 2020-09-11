package com.microservice.ruohanapp.controller;

import com.microservice.ruohanapp.feignclient.OrderClient;
import com.microservice.ruohanapp.po.OrderPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/API/orders")
public class OpenFeignController {
    @Autowired
    private OrderClient orderClient;

    @GetMapping("")
    public List<OrderPO> getOrders() {
        List<OrderPO> orders = orderClient.queryAll();
        return orders;
    }
}
