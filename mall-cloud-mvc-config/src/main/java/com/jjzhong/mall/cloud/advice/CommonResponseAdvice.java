package com.jjzhong.mall.cloud.advice;

import com.jjzhong.mall.cloud.annotation.IgnoreResponseAdvice;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局统一响应配置类
 */
@RestControllerAdvice(value = "com.jjzhong.mall.cloud")
public class CommonResponseAdvice implements ResponseBodyAdvice<Object> {
    /**
     * 判断方法或 Controller 否有 IgnoreResponseAdvice 注解，若有，则不返回统一响应
     * @param returnType the return type
     * @param converterType the selected converter type
     * @return 是否返回统一响应
     */
    @Override
    @SuppressWarnings("all")
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (returnType.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class))
            return false;
        if (returnType.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class))
            return false;
        return true;
    }

    /**
     * 判断返回对象的类型，并对响应进行包装
     * @param body the body to be written
     * @param returnType the return type of the controller method
     * @param selectedContentType the content type selected through content negotiation
     * @param selectedConverterType the converter type selected to write to the response
     * @param request the current request
     * @param response the current response
     * @return 统一响应对象
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null)
            return CommonResponse.success();
        else if (body instanceof CommonResponse)
            return body;
        else return CommonResponse.success(body);
    }
}
