package com.jjzhong.mall.cloud.common.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 用于处理用户账户请求的类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HandleUserReq {
    @NotBlank(message = "用户名不能为空")
    private String userName;
    @NotEmpty(message = "密码不能为空")
    @Size(min = 8, message = "密码最短为8位")
    private String password;
}
