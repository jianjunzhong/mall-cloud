package com.jjzhong.mall.cloud.cartorder.exception;

import com.jjzhong.mall.cloud.common.exception.MallException;

/**
 * 购物车订单服务异常类
 */
public class MallCartOrderServiceException extends MallException {
    public MallCartOrderServiceException() {
    }

    public MallCartOrderServiceException(Integer code, String message) {
        super(code, message);
    }

    public MallCartOrderServiceException(MallCartOrderServiceExceptionEnum mallCartOrderServiceExceptionEnum) {
        super(mallCartOrderServiceExceptionEnum.getCode(), mallCartOrderServiceExceptionEnum.getMessage());
    }
}
