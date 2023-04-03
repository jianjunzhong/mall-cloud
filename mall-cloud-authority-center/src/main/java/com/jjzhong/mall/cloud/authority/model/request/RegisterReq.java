package com.jjzhong.mall.cloud.authority.model.request;

import com.jjzhong.mall.cloud.common.model.request.HandleUserReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 注册请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterReq {
    @NotBlank(message = "用户名不能为空")
    private String userName;
    @NotEmpty(message = "密码不能为空")
    @Size(min = 8, message = "密码最短为8位")
    private String password;
    @NotBlank(message = "email不能为空")
    @Email(message = "email不合法")
    private String emailAddress;
    @NotBlank(message = "验证码不能为空")
    private String verificationCode;
}
