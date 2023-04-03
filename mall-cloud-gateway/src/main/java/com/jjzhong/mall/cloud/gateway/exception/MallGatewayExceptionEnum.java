package com.jjzhong.mall.cloud.gateway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 网关异常枚举
 */
@Getter
@AllArgsConstructor
public enum MallGatewayExceptionEnum {
    NEED_ADMIN(90001, "无管理员权限"),
    TOKEN_INVALID(90002, "Token无效或已过期");
    private final Integer code;
    private final String message;
}
