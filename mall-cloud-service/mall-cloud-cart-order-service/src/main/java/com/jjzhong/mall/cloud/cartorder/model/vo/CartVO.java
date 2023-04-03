package com.jjzhong.mall.cloud.cartorder.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 购物车 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartVO {
    private Integer id;

    private Integer productId;

    private Integer userId;

    private Integer quantity;

    private Integer selected;

    private Integer price;

    private Integer totalPrice;

    private String productName;

    private String productImage;
}
