package com.jjzhong.mall.cloud.categoryproduct.controller;

import com.github.pagehelper.PageInfo;
import com.jjzhong.mall.cloud.categoryproduct.exception.MallCategoryProductServiceException;
import com.jjzhong.mall.cloud.categoryproduct.feign.UploadClient;
import com.jjzhong.mall.cloud.categoryproduct.model.pojo.Product;
import com.jjzhong.mall.cloud.categoryproduct.model.request.AddProductReq;
import com.jjzhong.mall.cloud.categoryproduct.model.request.UpdateProductReq;
import com.jjzhong.mall.cloud.categoryproduct.service.ProductService;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import com.jjzhong.mall.cloud.sdk.product.request.UpdateStockReq;
import com.jjzhong.mall.cloud.sdk.upload.response.FileUploadRes;
import com.jjzhong.mall.cloud.sdk.upload.response.ImageUploadRes;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "管理员商品接口")
@Slf4j
@RestController
@Validated
public class ProductAdminController {
    @Autowired
    private ProductService productService;

    @Autowired
    private UploadClient uploadClient;

    @PostMapping("/admin/product/add")
    @Operation(description = "增加商品")
    public CommonResponse<Object> add(@Valid @RequestBody AddProductReq addProductReq) {
        productService.add(addProductReq);
        return CommonResponse.success();
    }

    @PostMapping("/admin/upload/image")
    @Operation(description = "上传图片")
    public CommonResponse<String> upload(@RequestParam("file") MultipartFile file) {
        CommonResponse<ImageUploadRes> response = uploadClient.uploadImage(file);
        if (response.isError())
            throw new MallCategoryProductServiceException(response.getStatus(), response.getMsg());
        return CommonResponse.success(response.getData().getUri());
    }

    @PostMapping("/admin/product/update")
    @Operation(description = "后台更新商品")
    public CommonResponse<Object> update(@Valid @RequestBody UpdateProductReq updateProductReq) {
        productService.update(updateProductReq);
        return CommonResponse.success();
    }

    @PostMapping("/admin/product/delete")
    @Operation(description = "后台删除商品")
    public CommonResponse<Object> delete(@RequestParam Integer id) {
        productService.delete(id);
        return CommonResponse.success();
    }

    @PostMapping("/admin/product/batchUpdateSellStatus")
    @Operation(description = "后台批量上下架商品")
    public CommonResponse<Object> batchUpdateSellStatus(@RequestParam Integer[] ids, @RequestParam Integer sellStatus) {
        productService.batchUpdateSellStatus(ids, sellStatus);
        return CommonResponse.success();
    }

    @GetMapping("/admin/product/list")
    @Operation(description = "后台商品列表")
    public CommonResponse<PageInfo<Product>> list(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        return CommonResponse.success(productService.listForAdmin(pageNum, pageSize));
    }

    @PostMapping("/admin/upload/product")
    @Operation(description = "后台批量上传商品")
    public CommonResponse<Object> uploadProduct(@RequestParam("file") MultipartFile file) {
        CommonResponse<FileUploadRes> response = uploadClient.uploadFile(file);
        if (response.isError())
            log.error("upload fail, please check upload service");
        productService.addProductByExcel(file);
        return CommonResponse.success();
    }

    @PostMapping("/admin/update/stock")
    @Operation(description = "更新商品库存")
    public CommonResponse<Object> updateStock(@Valid @RequestBody UpdateStockReq updateStockReq) {
        productService.updateStock(updateStockReq);
        return CommonResponse.success();
    }
}
