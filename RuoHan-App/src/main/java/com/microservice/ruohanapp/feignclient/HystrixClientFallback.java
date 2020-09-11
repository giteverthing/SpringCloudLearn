package com.microservice.ruohanapp.feignclient;

import com.microservice.ruohanapp.po.OrderPO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HystrixClientFallback implements OrderClient{

    @Override
    public List<OrderPO> queryAll() {
        throw new RuntimeException("调用失败！！！");
//        return null;
    }
}
