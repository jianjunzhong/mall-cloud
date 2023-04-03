package com.jjzhong.mall.cloud.sdk.email.response.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邮件校验状态
 */
@Getter
@AllArgsConstructor
public enum VerifyStatus {
    MATCHED(0, "验证成功"),
    EMAIL_NOT_SEND(1, "邮件未发送"),
    NOT_MATCH(2, "验证失败");
    private final int code;
    private final String msg;
}
