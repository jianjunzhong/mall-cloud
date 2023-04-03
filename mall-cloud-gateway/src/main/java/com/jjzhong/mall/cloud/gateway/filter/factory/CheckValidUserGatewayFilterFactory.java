package com.jjzhong.mall.cloud.gateway.filter.factory;

import com.jjzhong.mall.cloud.gateway.filter.CheckValidUserGatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

/**
 * 管理员校验类。当继承自 AbstractGatewayFilterFactory，并且类名后缀为 GatewayFilterFactory 时，配置的 filter 才会生效。
 */
@Component
public class CheckValidUserGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    @Override
    public GatewayFilter apply(Object config) {
        return new CheckValidUserGatewayFilter();
    }
}
