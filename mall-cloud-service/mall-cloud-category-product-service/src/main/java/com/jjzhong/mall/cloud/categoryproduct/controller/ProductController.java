package com.jjzhong.mall.cloud.categoryproduct.controller;

import com.github.pagehelper.PageInfo;
import com.jjzhong.mall.cloud.categoryproduct.model.request.QueryProductReq;
import com.jjzhong.mall.cloud.categoryproduct.service.ProductService;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import com.jjzhong.mall.cloud.sdk.product.request.RestoreStockReq;
import com.jjzhong.mall.cloud.sdk.product.request.DeductStockReq;
import com.jjzhong.mall.cloud.sdk.product.vo.ProductVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "商品接口")
@RestController
@RequestMapping("/product")
@Validated
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    @Operation(description = "查询商品列表")
    public CommonResponse<PageInfo<ProductVO>> list(QueryProductReq queryProductReq) {
        return CommonResponse.success(productService.queryFromDBAndCachedToRedis(queryProductReq));
    }

    @GetMapping("/detail")
    @Operation(description = "商品详情")
    public CommonResponse<ProductVO> detail(@RequestParam Integer id) {
        return CommonResponse.success(productService.detail(id));
    }

    @PostMapping("/restore")
    @Operation(description = "恢复商品库存")
    public CommonResponse<Object> restore(@Valid @RequestBody RestoreStockReq restoreStockReq) {
        productService.restoreStock(restoreStockReq);
        return CommonResponse.success();
    }

    @PostMapping("/deductStock")
    @Operation(description = "扣减商品库存")
    public CommonResponse<Object> deductStock(@RequestBody DeductStockReq deductStockReq) {
        productService.deductStock(deductStockReq);
        return CommonResponse.success();
    }
}
