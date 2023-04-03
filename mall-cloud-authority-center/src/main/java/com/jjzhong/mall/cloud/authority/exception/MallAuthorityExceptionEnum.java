package com.jjzhong.mall.cloud.authority.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 授权中心异常枚举
 */
@Getter
@AllArgsConstructor
public enum MallAuthorityExceptionEnum {
    USER_NAME_EXISTED(80001, "用户名已存在"),
    EMAIL_REGISTERED(80002, "Email已被注册"),
    INVALID_USER_NAME_OR_PASSWORD(80003, "用户名或密码错误"),
    INVALID_VERIFICATION_CODE(80004, "验证码错误，请新输入"),
    ;
    private final Integer code;
    private final String message;
}
