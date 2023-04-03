package com.jjzhong.mall.cloud.upload.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上传异常枚举
 */
@Getter
@AllArgsConstructor
public enum MallUploadServiceExceptionEnum {
    DIR_CREATE_FAILED(81001, "文件夹创建失败"),
    UPLOAD_FAILED(81002, "上传失败"),
    QRCODE_GENERATE_FAILED(81003, "二维码生成失败"),
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
