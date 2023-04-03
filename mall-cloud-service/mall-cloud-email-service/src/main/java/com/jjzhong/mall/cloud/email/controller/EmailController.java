package com.jjzhong.mall.cloud.email.controller;

import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import com.jjzhong.mall.cloud.email.service.EmailService;
import com.jjzhong.mall.cloud.sdk.email.request.EmailVerificationCodeCheckReq;
import com.jjzhong.mall.cloud.sdk.email.response.EmailSendRes;
import com.jjzhong.mall.cloud.sdk.email.response.EmailVerificationCodeCheckRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Tag(name = "电子邮件接口")
@RestController
@Validated
public class EmailController {
    @Autowired
    private EmailService emailService;
    @PostMapping("/email/send")
    @Operation(description = "发送验证码Email")
    public CommonResponse<EmailSendRes> sendVerificationCodeEmail(@RequestParam @NotBlank @Email String email) {
        return CommonResponse.success(emailService.sendVerifyCodeEmail(email));
    }

    @PostMapping("/email/check")
    @Operation(description = "检查验证码与邮箱是否匹配")
    public CommonResponse<EmailVerificationCodeCheckRes> checkEmailVerificationCodeIsMatch(
            @Valid @RequestBody EmailVerificationCodeCheckReq emailVerificationCodeCheckReq
    ) {
        return CommonResponse.success(emailService.checkIsMatch(emailVerificationCodeCheckReq));
    }
}
