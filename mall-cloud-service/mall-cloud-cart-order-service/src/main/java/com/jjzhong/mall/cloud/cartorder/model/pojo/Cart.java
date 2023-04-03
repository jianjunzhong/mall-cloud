package com.jjzhong.mall.cloud.cartorder.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 购物车 POJO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private Integer id;

    private Integer productId;

    private Integer userId;

    private Integer quantity;

    private Integer selected;

    private Date createTime;

    private Date updateTime;
}