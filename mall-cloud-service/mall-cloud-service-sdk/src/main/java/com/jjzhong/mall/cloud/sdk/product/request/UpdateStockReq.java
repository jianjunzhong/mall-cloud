package com.jjzhong.mall.cloud.sdk.product.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 更新库存请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStockReq {
    @NotNull
    private Integer productId;
    @NotNull
    private Integer stock;
}
