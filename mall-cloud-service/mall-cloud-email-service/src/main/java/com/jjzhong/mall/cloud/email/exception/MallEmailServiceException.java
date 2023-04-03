package com.jjzhong.mall.cloud.email.exception;

import com.jjzhong.mall.cloud.common.exception.MallException;

/**
 * 邮件服务异常
 */
public class MallEmailServiceException extends MallException {
    public MallEmailServiceException(MallEmailServiceExceptionEnum mallEmailServiceExceptionEnum) {
        super(mallEmailServiceExceptionEnum.getCode(), mallEmailServiceExceptionEnum.getMessage());
    }
}
