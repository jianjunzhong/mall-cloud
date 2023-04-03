package com.jjzhong.mall.cloud.conf;

import com.alibaba.cloud.seata.web.SeataHandlerInterceptor;
import com.jjzhong.mall.cloud.filter.CommonUserInfoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * WebMVC配置类
 */
@Configuration
public class MallWebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * 注册拦截器
     * @param registry 注册器
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        // 获取用户身份的拦截器
        registry.addInterceptor(new CommonUserInfoInterceptor())
                .addPathPatterns("/order/*")
                .addPathPatterns("/cart/*")
                .addPathPatterns("/user/update")
                .addPathPatterns("/user/logout")
                .addPathPatterns("/admin/category/*")
                .addPathPatterns("/admin/product/*")
                .addPathPatterns("/admin/order/*")
                .addPathPatterns("/product/deductStock")
                .order(0);
        // Seata 拦截并标记 XID
        registry.addInterceptor(new SeataHandlerInterceptor()).addPathPatterns("/**");
    }
}
