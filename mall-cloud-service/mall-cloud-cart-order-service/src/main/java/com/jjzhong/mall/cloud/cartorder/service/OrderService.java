package com.jjzhong.mall.cloud.cartorder.service;

import com.github.pagehelper.PageInfo;
import com.jjzhong.mall.cloud.cartorder.model.query.OrderStatisticsQuery;
import com.jjzhong.mall.cloud.cartorder.model.request.CreateOrderReq;
import com.jjzhong.mall.cloud.cartorder.model.vo.OrderStatisticsVO;
import com.jjzhong.mall.cloud.cartorder.model.vo.OrderVO;

import java.io.IOException;
import java.util.List;

public interface OrderService {
    String create(CreateOrderReq createOrderReq);

    OrderVO detail(String orderNo);

    PageInfo<OrderVO> listForCustomer(Integer pageNum, Integer pageSize);

    PageInfo<OrderVO> listForAdmin(Integer pageNum, Integer pageSize);

    void cancel(String orderNo);

    void pay(String orderNo);

    void deliver(String orderNo);

    void finish(String orderNo);

    List<OrderStatisticsVO> statistics(OrderStatisticsQuery orderStatisticsQuery);
}
