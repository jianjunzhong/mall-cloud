package com.jjzhong.mall.cloud.categoryproduct.constant;

import com.jjzhong.mall.cloud.categoryproduct.exception.MallCategoryProductServiceException;
import com.jjzhong.mall.cloud.categoryproduct.exception.MallCategoryProductServiceExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 用户查询商品时的排序枚举，防止 SQL 注入攻击
 */
@Getter
@AllArgsConstructor
public enum ProductListOrderByEnum {
    PRICE_DESC("price desc"),
    PRICE_ASC("price asc");
    private final String orderBy;

    public static ProductListOrderByEnum of(String orderBy_) {
        Objects.requireNonNull(orderBy_);
        return Stream.of(values())
                .filter(value -> value.getOrderBy().equals(orderBy_))
                .findAny()
                .orElseThrow(() -> new MallCategoryProductServiceException(MallCategoryProductServiceExceptionEnum.NO_SUCH_ORDER));
    }
}
