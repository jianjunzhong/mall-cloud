package com.jjzhong.mall.cloud.sdk.email.response;

import com.jjzhong.mall.cloud.sdk.email.response.status.VerifyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮件校验结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationCodeCheckRes {
    private String data;
    private VerifyStatus status;
}
