package com.jjzhong.mall.cloud.sdk.product.request;

import com.jjzhong.mall.cloud.sdk.product.dto.DeductStockDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 扣减库存的请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeductStockReq {
    private List<DeductStockDTO> dtos;
}
