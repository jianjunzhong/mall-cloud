package com.jjzhong.mall.cloud.cartorder.controller;

import com.jjzhong.mall.cloud.cartorder.model.vo.CartVO;
import com.jjzhong.mall.cloud.cartorder.service.CartService;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import com.jjzhong.mall.cloud.filter.AccessContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@Tag(name = "购物车接口")
@RestController
@RequestMapping("/cart")
@Validated
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/list")
    @Operation(description = "获取用户购物车列表")
    public CommonResponse<List<CartVO>> list() {
        return CommonResponse.success(cartService.list());
    }

    @PostMapping("/add")
    @Operation(description = "购物车添加商品")
    public CommonResponse<List<CartVO>> add(@RequestParam Integer productId, @RequestParam @Min(1) Integer count) {
        return CommonResponse.success(cartService.add(productId, count));
    }

    @PostMapping("/update")
    @Operation(description = "更新购物车商品数量")
    public CommonResponse<List<CartVO>> update(@RequestParam Integer productId, @RequestParam @Min(1) Integer count) {
        return CommonResponse.success(cartService.updateCount(productId, count));
    }

    @PostMapping("/delete")
    @Operation(description = "删除购物车中商品")
    public CommonResponse<List<CartVO>> delete(@RequestParam Integer productId) {
        return CommonResponse.success(cartService.delete(productId));
    }

    @PostMapping("/select")
    @Operation(description = "选择购物车中商品")
    public CommonResponse<List<CartVO>> select(@RequestParam Integer productId, @RequestParam Integer selected) {
        return CommonResponse.success(cartService.updateSelect(productId, selected));
    }

    @PostMapping("/selectAll")
    @Operation(description = "选择购物车中所有商品")
    public CommonResponse<List<CartVO>> selectAll(@RequestParam Integer selected) {
        return CommonResponse.success(cartService.updateSelectAll(selected));
    }
}
