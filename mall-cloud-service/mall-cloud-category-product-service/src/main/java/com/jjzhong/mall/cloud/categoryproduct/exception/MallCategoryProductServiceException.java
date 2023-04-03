package com.jjzhong.mall.cloud.categoryproduct.exception;

import com.jjzhong.mall.cloud.common.exception.MallException;

/**
 * 分类和商品服务异常类
 */
public class MallCategoryProductServiceException extends MallException {
    public MallCategoryProductServiceException() {
    }

    public MallCategoryProductServiceException(Integer code, String message) {
        super(code, message);
    }

    public MallCategoryProductServiceException(MallCategoryProductServiceExceptionEnum mallCategoryProductServiceExceptionEnum) {
        super(mallCategoryProductServiceExceptionEnum.getCode(), mallCategoryProductServiceExceptionEnum.getMessage());
    }
}
