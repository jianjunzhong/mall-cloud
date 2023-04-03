package com.jjzhong.mall.cloud.authority.controller;

import com.jjzhong.mall.cloud.authority.stream.VerificationCodeSendService;
import com.jjzhong.mall.cloud.common.constant.CommonConstant;
import com.jjzhong.mall.cloud.common.model.request.HandleUserReq;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import com.jjzhong.mall.cloud.common.model.dto.JwtToken;
import com.jjzhong.mall.cloud.authority.model.pojo.User;
import com.jjzhong.mall.cloud.authority.model.request.RegisterReq;
import com.jjzhong.mall.cloud.authority.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Tag(name = "授权中心接口")
@RestController
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private VerificationCodeSendService verificationCodeSendService;

    @PostMapping("/register")
    @Operation(description = "用户注册")
    public CommonResponse<Object> register(@Valid RegisterReq registerReq) {
        userService.register(registerReq);
        return CommonResponse.success();
    }

    @GetMapping("/login")
    @Operation(description = "用户登录")
    public CommonResponse<User> login(@Valid HandleUserReq handleUserReq) {
        return CommonResponse.success(userService.login(handleUserReq));
    }

    @PostMapping("/user/update")
    @Operation(description = "更新用户签名")
    public CommonResponse<Object> updateUserInfo(@RequestParam String signature) {
        userService.updateSignature(signature);
        return CommonResponse.success();
    }

    @PostMapping("/user/logout")
    @Operation(description = "注销用户")
    public CommonResponse<Object> logout(HttpSession session) {
        return CommonResponse.success();
    }

    @GetMapping("/adminLogin")
    @Operation(description = "登录管理员")
    public CommonResponse<User> adminLogin(@Valid HandleUserReq handleUserReq) {
        return CommonResponse.success(userService.adminLogin(handleUserReq));
    }

    @PostMapping("/user/sendEmail")
    @Operation(description = "发送注册验证码邮件")
    public CommonResponse<Object> sendEmail(
            @RequestParam @NotBlank(message = "邮箱不能为空") @Email(message = "邮箱格式错误") String emailAddress
    ) {
        // 先验证邮箱是否已被注册
        userService.checkEmailRegistered(emailAddress);
        // 向消息队列发送消息
        verificationCodeSendService.sendMessage(emailAddress);
        return CommonResponse.success();
    }

    @GetMapping("/loginWithJwt")
    @Operation(description = "JWT登录")
    public CommonResponse<String> loginWithJwt(@Valid HandleUserReq handleUserReq) throws Exception {
        JwtToken jwtToken = userService.loginWithJwt(handleUserReq);
        return CommonResponse.success(jwtToken.getToken());
    }
}
