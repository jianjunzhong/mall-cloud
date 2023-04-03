package com.jjzhong.mall.cloud.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * 请求耗时统计过滤器类
 */
@Slf4j
@Component
public class GlobalElapsedLogFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        StopWatch stopWatch = StopWatch.createStarted();
        String uri = exchange.getRequest().getURI().getPath();
        return chain.filter(exchange).then(
            Mono.fromRunnable(() -> {
                log.info("URI: {}, elapsed: {} ms", uri, stopWatch.getTime(TimeUnit.MILLISECONDS));
                stopWatch.stop();
            })
        );
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
