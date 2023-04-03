package com.jjzhong.mall.cloud.common.exception;

/**
 * 全局异常抽象类
 */
public abstract class MallException extends RuntimeException {
    private final Integer code;
    private final String message;

    public MallException() {
        this.code = MallCommonExceptionEnum.UNKNOWN_ERROR.getCode();
        this.message = MallCommonExceptionEnum.UNKNOWN_ERROR.getMessage();
    }

    public MallException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
