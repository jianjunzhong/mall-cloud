package com.jjzhong.mall.cloud.authority.exception;

import com.jjzhong.mall.cloud.common.exception.MallException;

/**
 * 授权中心异常类
 */
public class MallAuthorityException extends MallException {
    public MallAuthorityException(MallAuthorityExceptionEnum mallServiceExceptionEnum) {
        super(mallServiceExceptionEnum.getCode(), mallServiceExceptionEnum.getMessage());
    }
}
