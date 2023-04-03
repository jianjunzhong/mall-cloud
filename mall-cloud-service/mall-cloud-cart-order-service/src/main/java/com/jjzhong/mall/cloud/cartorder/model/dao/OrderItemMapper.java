package com.jjzhong.mall.cloud.cartorder.model.dao;


import com.jjzhong.mall.cloud.cartorder.model.pojo.OrderItem;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem row);

    int insertSelective(OrderItem row);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem row);

    int updateByPrimaryKey(OrderItem row);
    List<OrderItem> selectListByOrderNo(String orderNo);
}