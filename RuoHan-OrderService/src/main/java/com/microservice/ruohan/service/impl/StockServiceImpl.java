package com.microservice.ruohan.service.impl;

import com.microservice.ruohan.repository.StockMapper;
import com.microservice.ruohan.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockServiceImpl implements StockService {
    @Autowired
    private StockMapper mapper;

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.REPEATABLE_READ, rollbackFor = {ArithmeticException.class, Exception.class, RuntimeException.class})
    public int update(int productId) {

        int affectRows = mapper.update(productId);
//        int b = 1 / 0;
        if (affectRows < 1) {
//            throw new Exception("Stock is not enough.");
        }
        return affectRows;
    }
}
