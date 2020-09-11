package com.microservice.ruohanapp.feignclient;

import com.microservice.ruohanapp.po.OrderPO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "order-service", fallback = HystrixClientFallback.class)
public interface OrderClient {
    @GetMapping("order")
    List<OrderPO> queryAll();
}


