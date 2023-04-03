package com.jjzhong.mall.cloud.sdk.product.message;

import com.jjzhong.mall.cloud.sdk.product.request.RestoreStockReq;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 恢复库存的消息
 */
public class RestoreStockMsg extends RestoreStockReq {
    public RestoreStockMsg(@NotNull Integer productId, @NotNull @Min(1) Integer quantity) {
        super(productId, quantity);
    }
}
