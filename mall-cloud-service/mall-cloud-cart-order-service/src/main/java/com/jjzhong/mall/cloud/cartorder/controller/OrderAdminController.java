package com.jjzhong.mall.cloud.cartorder.controller;

import com.github.pagehelper.PageInfo;
import com.jjzhong.mall.cloud.cartorder.model.query.OrderStatisticsQuery;
import com.jjzhong.mall.cloud.cartorder.model.vo.OrderStatisticsVO;
import com.jjzhong.mall.cloud.cartorder.model.vo.OrderVO;
import com.jjzhong.mall.cloud.cartorder.service.OrderService;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "管理员后台订单接口")
@RestController
@Validated
public class OrderAdminController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/admin/order/list")
    @Operation(description = "后台订单列表")
    public CommonResponse<PageInfo<OrderVO>> listForAdmin(@RequestParam Integer pageNum, Integer pageSize) {
        return CommonResponse.success(orderService.listForAdmin(pageNum, pageSize));
    }

    @PostMapping("/admin/order/delivered")
    @Operation(description = "支付")
    public CommonResponse<Object> deliver(@RequestParam String orderNo) {
        orderService.deliver(orderNo);
        return CommonResponse.success();
    }

    @GetMapping("/admin/order/statistics")
    @Operation(description = "每日订单量统计")
    public CommonResponse<List<OrderStatisticsVO>> statistics(@Valid OrderStatisticsQuery orderStatisticsQuery) {
        return CommonResponse.success(orderService.statistics(orderStatisticsQuery));
    }
}
