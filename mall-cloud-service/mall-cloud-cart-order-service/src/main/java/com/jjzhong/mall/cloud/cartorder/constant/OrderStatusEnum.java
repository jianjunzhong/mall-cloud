package com.jjzhong.mall.cloud.cartorder.constant;

import com.jjzhong.mall.cloud.cartorder.exception.MallCartOrderServiceException;
import com.jjzhong.mall.cloud.cartorder.exception.MallCartOrderServiceExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 订单状态枚举
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    CANCELED(0, "用户已取消"),
    NOT_PAY(10, "未付款"),
    PAID(20, "已付款"),
    DELIVERED(30, "已发货"),
    FINISHED(40, "交易完成");
    private Integer code;
    private String name;
    public static OrderStatusEnum of(Integer code_) {
        return Stream.of(values())
                .filter(value -> value.getCode().equals(code_))
                .findAny()
                .orElseThrow(() ->
                        new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_STATUS_INCORRECT)
                );
    }
}
