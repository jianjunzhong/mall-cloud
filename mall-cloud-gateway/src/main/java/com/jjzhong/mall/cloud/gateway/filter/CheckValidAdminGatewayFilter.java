package com.jjzhong.mall.cloud.gateway.filter;

import com.jjzhong.mall.cloud.common.constant.CommonConstant;
import com.jjzhong.mall.cloud.common.model.vo.CommonUserInfo;
import com.jjzhong.mall.cloud.gateway.exception.MallGatewayException;
import com.jjzhong.mall.cloud.gateway.exception.MallGatewayExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 验证是否为管理员的网关过滤器
 */
@Slf4j
public class CheckValidAdminGatewayFilter implements GatewayFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpMethod method = exchange.getRequest().getMethod();
        if (method == null || !method.equals(HttpMethod.OPTIONS)) {
            CommonUserInfo commonUserInfo = CheckValidUserGatewayFilter.checkJwtToken(exchange);
            if (commonUserInfo == null) {
                throw new MallGatewayException(MallGatewayExceptionEnum.TOKEN_INVALID);
            } else if (!commonUserInfo.getRole().equals(CommonConstant.Role.ADMIN.getCode())) {
                throw new MallGatewayException(MallGatewayExceptionEnum.NEED_ADMIN);
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
