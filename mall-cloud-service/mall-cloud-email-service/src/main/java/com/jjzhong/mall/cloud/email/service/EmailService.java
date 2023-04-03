package com.jjzhong.mall.cloud.email.service;

import com.jjzhong.mall.cloud.sdk.email.request.EmailVerificationCodeCheckReq;
import com.jjzhong.mall.cloud.sdk.email.response.EmailSendRes;
import com.jjzhong.mall.cloud.sdk.email.response.EmailVerificationCodeCheckRes;

public interface EmailService {

    EmailSendRes sendVerifyCodeEmail(String email);

    EmailVerificationCodeCheckRes checkIsMatch(EmailVerificationCodeCheckReq emailVerificationCodeCheckReq);
}
