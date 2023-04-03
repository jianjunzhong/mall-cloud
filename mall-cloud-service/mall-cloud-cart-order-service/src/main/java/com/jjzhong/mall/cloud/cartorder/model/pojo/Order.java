package com.jjzhong.mall.cloud.cartorder.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 订单 POJO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Integer id;

    private String orderNo;

    private Integer userId;

    private Integer totalPrice;

    private String receiverName;

    private String receiverMobile;

    private String receiverAddress;

    private Integer orderStatus;

    /** 运费 */
    private Integer postage;

    /** 付款方式 */
    private Integer paymentType;

    private Date deliveryTime;

    private Date payTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;
}