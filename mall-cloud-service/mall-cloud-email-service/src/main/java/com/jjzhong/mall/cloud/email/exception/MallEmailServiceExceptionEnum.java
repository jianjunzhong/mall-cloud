package com.jjzhong.mall.cloud.email.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邮件服务异常枚举
 */
@Getter
@AllArgsConstructor
public enum MallEmailServiceExceptionEnum {
    INCORRECT_EMAIL_ADDRESS(82001, "Email地址错误"),
    EMAIL_NOT_SEND(82002, "未发送验证邮件"),
    EMAIL_AND_VERIFICATION_CODE_NOT_MATCH(82003, "验证码不匹配"),
    EMAIL_SENT(82004, "验证码已发送，请稍后再试"),
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
