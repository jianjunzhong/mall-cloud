package com.jjzhong.mall.cloud.cartorder.config;

import com.jjzhong.mall.cloud.common.constant.CommonConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 此拦截器可在 Feign 对其他服务的请求中带上原始请求头中 jwt_token 的请求头
 */
@Configuration
public class FeignRequestInterceptor {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 从 Spring 的 RequestContextHolder 中获取原始请求头
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    template.header(CommonConstant.JWT_TOKEN, request.getHeader(CommonConstant.JWT_TOKEN));
                }
            }
        };
    }
}