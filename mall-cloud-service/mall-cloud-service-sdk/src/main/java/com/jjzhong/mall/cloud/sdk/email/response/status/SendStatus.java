package com.jjzhong.mall.cloud.sdk.email.response.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邮件发送状态
 */
@Getter
@AllArgsConstructor
public enum SendStatus {
    SUCCESS(0, "发送成功"),
    ERROR(1, "发送失败");
    private final int code;
    private final String msg;
}
