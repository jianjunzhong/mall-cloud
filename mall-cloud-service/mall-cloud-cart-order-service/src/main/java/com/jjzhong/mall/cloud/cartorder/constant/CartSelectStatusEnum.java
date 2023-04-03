package com.jjzhong.mall.cloud.cartorder.constant;

import com.jjzhong.mall.cloud.cartorder.exception.MallCartOrderServiceException;
import com.jjzhong.mall.cloud.cartorder.exception.MallCartOrderServiceExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 购物车物品选择状态枚举
 */
@Getter
@AllArgsConstructor
public enum CartSelectStatusEnum {
    UN_SELECTED(0),
    SELECTED(1);
    private final Integer status;

    public static CartSelectStatusEnum of(Integer status_) {
        Objects.requireNonNull(status_);
        return Stream.of(values())
                .filter(value -> value.getStatus().equals(status_))
                .findAny()
                .orElseThrow(() -> new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.NO_SUCH_SELECTED_STATUS));
    }
}