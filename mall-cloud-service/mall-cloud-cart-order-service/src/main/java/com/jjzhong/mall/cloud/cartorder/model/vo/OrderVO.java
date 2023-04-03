package com.jjzhong.mall.cloud.cartorder.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 订单 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO {
    private String orderNo;

    private Integer userId;

    private Integer totalPrice;

    private String receiverName;

    private String receiverMobile;

    private String receiverAddress;

    private Integer orderStatus;

    private Integer postage;

    private Integer paymentType;

    private Date deliveryTime;

    private Date payTime;

    private Date endTime;

    private Date createTime;
    private List<OrderItemVO> orderItemVOList;
    private String orderStatusName;
}
