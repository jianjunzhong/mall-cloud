package com.jjzhong.mall.cloud.gateway.filter;

import com.jjzhong.mall.cloud.common.constant.CommonConstant;
import com.jjzhong.mall.cloud.common.exception.MallCommonException;
import com.jjzhong.mall.cloud.common.exception.MallCommonExceptionEnum;
import com.jjzhong.mall.cloud.common.model.vo.CommonUserInfo;
import com.jjzhong.mall.cloud.common.util.TokenParseUtil;
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
 * 验证是否为登陆用户的网关过滤器
 */
@Slf4j
public class CheckValidUserGatewayFilter implements GatewayFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpMethod method = exchange.getRequest().getMethod();
        /* 预检请求直接放行 */
        if (method == null || !method.equals(HttpMethod.OPTIONS)) {
            CommonUserInfo commonUserInfo = checkJwtToken(exchange);
            if (commonUserInfo == null) {
                throw new MallCommonException(MallCommonExceptionEnum.TOKEN_INVALID);
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 从请求中获取用户信息的静态方法
     * @param exchange exchange
     * @return 用户信息
     */
    public static CommonUserInfo checkJwtToken(ServerWebExchange exchange) {
        String jwt_token = exchange.getRequest().getHeaders().getFirst(CommonConstant.JWT_TOKEN);
        if (jwt_token == null) {
            throw new MallCommonException(MallCommonExceptionEnum.NEED_LOGIN);
        }
        CommonUserInfo commonUserInfo = null;
        try {
            commonUserInfo = TokenParseUtil.parseCommonUserInfoFromToken(jwt_token);
        } catch (Exception e) {
            log.info("parse common user info from JWT error");
        }
        return commonUserInfo;
    }
}
