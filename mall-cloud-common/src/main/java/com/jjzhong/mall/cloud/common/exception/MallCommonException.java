package com.jjzhong.mall.cloud.common.exception;

/**
 * 公共异常类
 */
public class MallCommonException extends MallException{
    public MallCommonException(MallCommonExceptionEnum mallServiceExceptionEnum) {
        super(mallServiceExceptionEnum.getCode(), mallServiceExceptionEnum.getMessage());
    }
}
