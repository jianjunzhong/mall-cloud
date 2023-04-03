package com.jjzhong.mall.cloud.sdk.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVO {
    private Integer id;

    private String name;

    private String image;

    private String detail;

    private Integer categoryId;

    private Integer price;

    private Integer stock;

    private Integer status;
}