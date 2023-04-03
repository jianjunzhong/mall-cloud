package com.jjzhong.mall.cloud.cartorder.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 购物车订单服务异常枚举
 */
@Getter
@AllArgsConstructor
public enum MallCartOrderServiceExceptionEnum {
    NO_SUCH_SELECTED_STATUS(20001, "找不到对应的选择状态"),
    STOCK_NOT_ENOUGH(20002, "库存不足"),
    CART_EMPTY_OR_NOT_SELECTED(20003, "购物车为空或未勾选商品"),
    STOCK_UPDATE_FAILED(20004, "库存扣减失败"),
    ORDER_STATUS_INCORRECT(20005, "订单状态异常"),
    ORDER_NOT_FOUND(20006, "未找到对应的订单"),
    ORDER_NOT_MATCH(20007, "订单不匹配"),
    PAY_QRCODE_GENERATE_FAILED(20008, "支付二维码生成失败"),
    CATEGORY_PRODUCT_SERVICE_ERROR(20009, "商品服务异常，请稍后再试"),
    ORDER_CANCELED(20010, "订单已取消，无法操作"),
    ORDER_PAID(20011, "订单已取消，无法操作"),
    ORDER_NOT_PAID(20012, "订单未付款，无法操作"),
    ORDER_DELIVERED(20013, "订单已发货，无法操作"),
    ORDER_NOT_DELIVERED(20014, "订单未发货，无法操作"),
    ORDER_FINISHED(20015, "订单已完结，无法操作"),
    ;
    private final Integer code;
    private final String message;
    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
