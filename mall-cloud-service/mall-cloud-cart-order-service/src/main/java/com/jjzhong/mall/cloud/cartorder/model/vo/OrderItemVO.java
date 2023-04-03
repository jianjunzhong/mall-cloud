package com.jjzhong.mall.cloud.cartorder.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单商品 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemVO {
    private String orderNo;
    private Integer productId;

    private String productName;

    private String productImg;

    private Integer unitPrice;

    private Integer quantity;

    private Integer totalPrice;
}
