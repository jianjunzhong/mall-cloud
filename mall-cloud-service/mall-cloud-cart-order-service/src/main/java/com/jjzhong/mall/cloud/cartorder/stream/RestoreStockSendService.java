package com.jjzhong.mall.cloud.cartorder.stream;

import com.alibaba.fastjson.JSON;
import com.jjzhong.mall.cloud.sdk.product.message.RestoreStockMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

@Slf4j
@EnableBinding(RestoreStockSource.class)
public class RestoreStockSendService {
    @Autowired
    private RestoreStockSource restoreStockSource;

    public void sendMessage(RestoreStockMsg restoreStockMsg) {
        String message = JSON.toJSONString(restoreStockMsg);
        log.info("send message in RestoreStockSendService: {}", message);
        restoreStockSource.restoreStockOutput().send(
                MessageBuilder.withPayload(message).build()
        );
    }
}
