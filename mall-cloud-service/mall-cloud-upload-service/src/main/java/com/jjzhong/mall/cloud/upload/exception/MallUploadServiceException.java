package com.jjzhong.mall.cloud.upload.exception;

import com.jjzhong.mall.cloud.common.exception.MallException;

/**
 * 上传服务异常类
 */
public class MallUploadServiceException extends MallException {
    public MallUploadServiceException(MallUploadServiceExceptionEnum mallUploadServiceExceptionEnum) {
        super(mallUploadServiceExceptionEnum.getCode(), mallUploadServiceExceptionEnum.getMessage());
    }
}
