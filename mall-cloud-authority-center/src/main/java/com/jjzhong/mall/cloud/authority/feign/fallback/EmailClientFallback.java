package com.jjzhong.mall.cloud.authority.feign.fallback;

import com.jjzhong.mall.cloud.authority.feign.EmailClient;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import com.jjzhong.mall.cloud.sdk.email.request.EmailVerificationCodeCheckReq;
import com.jjzhong.mall.cloud.sdk.email.response.EmailVerificationCodeCheckRes;

/**
 * EmailService 异常熔断类
 */
public class EmailClientFallback implements EmailClient {
    @Override
    public CommonResponse<EmailVerificationCodeCheckRes> checkEmailVerificationCodeIsMatch(EmailVerificationCodeCheckReq emailVerificationCodeCheckReq) {
        return CommonResponse.error();
    }
}
