package com.jjzhong.mall.cloud.cartorder.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 订单中的商品 POJO，保存了下单时的商品快照
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private Integer id;

    private String orderNo;

    private Integer productId;

    private String productName;

    private String productImg;

    private Integer unitPrice;

    private Integer quantity;

    private Integer totalPrice;

    private Date createTime;

    private Date updateTime;
}