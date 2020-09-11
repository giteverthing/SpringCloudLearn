package com.microservice.ruohanapp.controller;

import com.microservice.ruohanapp.po.OrderPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/API/order")
public class APIController {
    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_API_URL = "http://order-service/";

    @GetMapping("")
    public String getOrders() {
        List<OrderPO> orders = restTemplate.getForObject(BASE_API_URL + "order", List.class);
        return orders.toString();
    }
}
