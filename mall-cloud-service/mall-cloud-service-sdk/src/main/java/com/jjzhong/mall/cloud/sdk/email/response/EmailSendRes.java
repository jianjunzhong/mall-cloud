package com.jjzhong.mall.cloud.sdk.email.response;

import com.jjzhong.mall.cloud.sdk.email.response.status.SendStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮件发送结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailSendRes {
    private String data;
    private SendStatus status;
}
