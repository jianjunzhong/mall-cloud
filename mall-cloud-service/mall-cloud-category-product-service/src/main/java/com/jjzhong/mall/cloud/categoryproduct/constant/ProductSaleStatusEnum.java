package com.jjzhong.mall.cloud.categoryproduct.constant;

import com.jjzhong.mall.cloud.categoryproduct.exception.MallCategoryProductServiceException;
import com.jjzhong.mall.cloud.categoryproduct.exception.MallCategoryProductServiceExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 商品上架状态枚举
 */
@Getter
@AllArgsConstructor
public enum ProductSaleStatusEnum {
    NOT_ON_SALE(0),
    ON_SALE(1);
    private final Integer status;

    public static ProductSaleStatusEnum of(Integer status_) {
        Objects.requireNonNull(status_);
        return Stream.of(values())
                .filter(value -> value.getStatus().equals(status_))
                .findAny()
                .orElseThrow(() -> new MallCategoryProductServiceException(MallCategoryProductServiceExceptionEnum.NO_SUCH_SALE_STATUS));
    }
}