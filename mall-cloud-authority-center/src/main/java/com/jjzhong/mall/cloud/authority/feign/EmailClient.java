package com.jjzhong.mall.cloud.authority.feign;

import com.jjzhong.mall.cloud.authority.feign.fallback.EmailClientFallback;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import com.jjzhong.mall.cloud.sdk.email.request.EmailVerificationCodeCheckReq;
import com.jjzhong.mall.cloud.sdk.email.response.EmailSendRes;
import com.jjzhong.mall.cloud.sdk.email.response.EmailVerificationCodeCheckRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * OpenFeign EmailService 客户端接口
 */
@FeignClient(
        value = "mall-cloud-email-service",
        fallback = EmailClientFallback.class
)
@Primary
public interface EmailClient {
    @RequestMapping(
            value = "/mall-cloud-email-service/email/check",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE
    )
    CommonResponse<EmailVerificationCodeCheckRes> checkEmailVerificationCodeIsMatch(EmailVerificationCodeCheckReq emailVerificationCodeCheckReq);
}