package com.jjzhong.mall.cloud.cartorder.feign;

import com.jjzhong.mall.cloud.cartorder.feign.fallback.ProductClientFallback;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import com.jjzhong.mall.cloud.sdk.product.request.RestoreStockReq;
import com.jjzhong.mall.cloud.sdk.product.request.DeductStockReq;
import com.jjzhong.mall.cloud.sdk.product.vo.ProductVO;
import com.jjzhong.mall.cloud.sdk.product.request.UpdateStockReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.*;

/**
 * OpenFeign 的商品服务调用客户端
 */
@FeignClient(
        value = "mall-cloud-category-product-service",
        fallback = ProductClientFallback.class
)
@Primary
public interface ProductClient {
    @RequestMapping(
            value = "/mall-cloud-category-product-service/product/detail",
            method = RequestMethod.GET,
            consumes = "application/json", produces = "application/json"
    )
    CommonResponse<ProductVO> detail(@RequestParam Integer id);

    @RequestMapping(
            value = "/mall-cloud-category-product-service/admin/update/stock",
            method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json"
    )
    CommonResponse<Object> updateStock(@RequestBody UpdateStockReq updateStockReq);

    @RequestMapping(
            value = "/mall-cloud-category-product-service/product/restore",
            method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json"
    )
    CommonResponse<Object> restore(@RequestBody RestoreStockReq restoreStockReq);

    @RequestMapping(
            value = "/mall-cloud-category-product-service/product/deductStock",
            method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json"
    )
    CommonResponse<Object> deductStock(@RequestBody DeductStockReq deductStockReq);
}
