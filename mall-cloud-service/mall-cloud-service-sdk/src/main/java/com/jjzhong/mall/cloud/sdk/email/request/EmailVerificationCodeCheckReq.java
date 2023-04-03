package com.jjzhong.mall.cloud.sdk.email.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 邮件验证码校验请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationCodeCheckReq {
    @NotBlank(message = "电子邮件不能为空")
    @Email(message = "电子邮件格式有误")
    private String emailAddress;
    @NotBlank(message = "验证码不能为空")
    private String verificationCode;
}
