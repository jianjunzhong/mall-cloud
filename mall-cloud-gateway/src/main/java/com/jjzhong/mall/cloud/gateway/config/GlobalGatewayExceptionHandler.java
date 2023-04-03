package com.jjzhong.mall.cloud.gateway.config;

import com.alibaba.fastjson.JSON;
import com.jjzhong.mall.cloud.common.exception.MallCommonException;
import com.jjzhong.mall.cloud.common.exception.MallCommonExceptionEnum;
import com.jjzhong.mall.cloud.common.exception.MallException;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import com.jjzhong.mall.cloud.gateway.exception.MallGatewayException;
import com.jjzhong.mall.cloud.gateway.exception.MallGatewayExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufMono;

/**
 * Gateway 全局统一异常处理
 */
@Slf4j
@Order(-1)
@Configuration
public class GlobalGatewayExceptionHandler implements ErrorWebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        CommonResponse<Object> commonResponse;
        // 当校验用户不通过时，会抛出异常
        if (ex instanceof MallException) {
            commonResponse = CommonResponse.error((MallException) ex);
        // 处理404等状态码异常
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException e = (ResponseStatusException) ex;
            commonResponse = CommonResponse.error(e.getRawStatusCode(), e.getMessage());
        } else {
            log.error("unexpected error: [{}]", ex.getMessage(), ex);
            commonResponse = CommonResponse.error(new MallCommonException(MallCommonExceptionEnum.SYSTEM_ERROR));
        }
        byte[] jsonBytes = JSON.toJSONBytes(commonResponse);
        DataBuffer dataBuffer = response.bufferFactory().allocateBuffer(jsonBytes.length).write(jsonBytes);
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeAndFlushWith(Mono.just(ByteBufMono.just(dataBuffer)));
    }
}
