package com.jjzhong.mall.cloud.common.exception;

/**
 * 公共异常枚举
 */
public enum MallCommonExceptionEnum {
    NEED_LOGIN(1001, "未登陆"),
    NEED_ADMIN(1002, "无管理员权限"),
    UPDATE_FAILED(1003, "更新失败"),
    INSERT_FAILED(1004, "新增失败"),
    DELETE_FAILED(1005, "删除失败"),
    REQUEST_PARAM_ERROR(1006, "参数错误"),
    TOKEN_INVALID(1007, "Token无效或已过期"),
    UPLOAD_SERVICE_ERROR(1008, "上传接口错误"),
    SYSTEM_ERROR(2000, "系统错误"),
    UNKNOWN_ERROR(-1, "未知错误");

    private Integer code;
    private String message;

    MallCommonExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

