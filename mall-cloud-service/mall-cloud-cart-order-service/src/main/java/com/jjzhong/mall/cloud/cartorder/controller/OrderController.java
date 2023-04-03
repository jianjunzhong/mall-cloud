package com.jjzhong.mall.cloud.cartorder.controller;

import com.github.pagehelper.PageInfo;
import com.jjzhong.mall.cloud.cartorder.exception.MallCartOrderServiceException;
import com.jjzhong.mall.cloud.cartorder.exception.MallCartOrderServiceExceptionEnum;
import com.jjzhong.mall.cloud.cartorder.feign.UploadClient;
import com.jjzhong.mall.cloud.cartorder.model.request.CreateOrderReq;
import com.jjzhong.mall.cloud.cartorder.model.vo.OrderVO;
import com.jjzhong.mall.cloud.cartorder.service.OrderService;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import com.jjzhong.mall.cloud.sdk.upload.response.ImageUploadRes;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "普通用户订单接口")
@RestController
@Validated
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UploadClient uploadClient;

    @PostMapping("/order/create")
    @Operation(description = "创建订单")
    public CommonResponse<String> create(@Valid @RequestBody CreateOrderReq createOrderReq) {
        return CommonResponse.success(orderService.create(createOrderReq));
    }

    @GetMapping("/order/detail")
    @Operation(description = "前台订单详情")
    public CommonResponse<OrderVO> detail(@RequestParam String orderNo) {
        return CommonResponse.success(orderService.detail(orderNo));
    }

    @GetMapping("/order/list")
    @Operation(description = "前台订单列表")
    public CommonResponse<PageInfo<OrderVO>> detail(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        return CommonResponse.success(orderService.listForCustomer(pageNum, pageSize));
    }

    @PostMapping("/order/cancel")
    @Operation(description = "前台取消订单")
    public CommonResponse<Object> cancel(@RequestParam String orderNo) {
        orderService.cancel(orderNo);
        return CommonResponse.success();
    }

    @GetMapping("/order/qrcode")
    @Operation(description = "生成支付二维码")
    public CommonResponse<String> qrCode(@RequestParam String orderNo) {
        CommonResponse<ImageUploadRes> response = uploadClient.generateQrCode(orderNo);
        if (response.isError())
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.PAY_QRCODE_GENERATE_FAILED);
        return CommonResponse.success(response.getData().getUri());
    }

    @GetMapping("/pay")
    @Operation(description = "支付")
    public CommonResponse<Object> pay(@RequestParam String orderNo) {
        orderService.pay(orderNo);
        return CommonResponse.success();
    }

    @PostMapping("/order/finish")
    @Operation(description = "完结")
    public CommonResponse<Object> finish(@RequestParam String orderNo) {
        orderService.finish(orderNo);
        return CommonResponse.success();
    }
}
