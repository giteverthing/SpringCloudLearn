package com.microservice.ruohan.repository;

import com.microservice.ruohan.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Select("select * from `order`")
    List<Order> queryAll();

    @Insert("insert into `order`(buyer_id,order_no,product_id) values(#{entity.buyerId},#{entity.orderNo},#{entity.productId})")
    int insert(@Param("entity") Order order);
}
