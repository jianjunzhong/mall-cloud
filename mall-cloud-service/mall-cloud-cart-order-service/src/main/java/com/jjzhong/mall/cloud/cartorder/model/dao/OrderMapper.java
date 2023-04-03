package com.jjzhong.mall.cloud.cartorder.model.dao;


import com.jjzhong.mall.cloud.cartorder.model.pojo.Order;
import com.jjzhong.mall.cloud.cartorder.model.query.OrderStatisticsQuery;
import com.jjzhong.mall.cloud.cartorder.model.vo.OrderStatisticsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order row);

    int insertSelective(Order row);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order row);

    int updateByPrimaryKey(Order row);

    Order selectByOrderNo(String orderNo);
    List<Order> selectByUserId(Integer userId);

    List<Order> selectList();
    List<OrderStatisticsVO> selectOrderStatistics(@Param("query") OrderStatisticsQuery query);
}