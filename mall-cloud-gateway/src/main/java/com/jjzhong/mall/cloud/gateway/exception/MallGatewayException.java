package com.jjzhong.mall.cloud.gateway.exception;

import com.jjzhong.mall.cloud.common.exception.MallException;

/**
 * 网关异常类
 */
public class MallGatewayException extends MallException {
    public MallGatewayException(Integer code, String message) {
        super(code, message);
    }

    public MallGatewayException(MallGatewayExceptionEnum mallGatewayExceptionEnum) {
        this(mallGatewayExceptionEnum.getCode(), mallGatewayExceptionEnum.getMessage());
    }
}
