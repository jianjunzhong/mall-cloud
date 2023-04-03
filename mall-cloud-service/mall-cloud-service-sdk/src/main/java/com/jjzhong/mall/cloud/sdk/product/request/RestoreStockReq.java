package com.jjzhong.mall.cloud.sdk.product.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 恢复库存请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestoreStockReq {
    @NotNull
    private Integer productId;
    @NotNull
    @Min(1)
    private Integer quantity;
}
