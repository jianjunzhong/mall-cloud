package com.jjzhong.mall.cloud.sdk.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 扣减商品库存的数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeductStockDTO {
    @Schema(description = "商品 id")
    private Integer productId;
    @Schema(description = "扣减数量")
    private Integer count;
}
