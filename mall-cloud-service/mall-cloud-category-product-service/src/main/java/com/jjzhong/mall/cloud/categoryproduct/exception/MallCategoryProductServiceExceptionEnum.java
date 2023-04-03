package com.jjzhong.mall.cloud.categoryproduct.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分类和商品服务异常枚举
 */
@Getter
@AllArgsConstructor
public enum MallCategoryProductServiceExceptionEnum {
    NAME_EXISTED(10001, "名称已存在"),
    NO_SUCH_ORDER(10002, "找不到对应的排序规则"),
    PRODUCT_NOT_FOUND(10003, "商品不存在或已下架"),
    INVALID_STOCK_DEDUCT_COUNT(10004, "商品库存扣减失败：扣减数量不合法"),
    STOCK_NOT_ENOUGH(10005, "商品库存扣减失败：库存不足"),
    PRODUCT_IMPORT_ERROR(10006, "商品导入异常"),
    NO_SUCH_SALE_STATUS(10007, "找不到对应的上架状态"),
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
