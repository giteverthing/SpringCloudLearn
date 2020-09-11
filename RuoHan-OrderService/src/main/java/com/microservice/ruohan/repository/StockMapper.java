package com.microservice.ruohan.repository;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface StockMapper {

    @Update("update stock set quantity=quantity-1 where product_id=#{productId} and quantity>1")
    int update(@Param("productId") int productId);
}
