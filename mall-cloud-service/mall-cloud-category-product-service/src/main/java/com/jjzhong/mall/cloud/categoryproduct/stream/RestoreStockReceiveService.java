package com.jjzhong.mall.cloud.categoryproduct.stream;

import com.alibaba.fastjson.JSON;
import com.jjzhong.mall.cloud.categoryproduct.service.ProductService;
import com.jjzhong.mall.cloud.sdk.product.message.RestoreStockMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * Stream 接收消息的服务
 */
@Slf4j
@EnableBinding(RestoreStockSink.class)
public class RestoreStockReceiveService {
    @Autowired
    private ProductService productService;

    /**
     * 接收恢复库存消息
     * @param payload 消息负载
     */
    @StreamListener(RestoreStockSink.INPUT)
    public void receiveMessage(@Payload Object payload) {
        log.info("start consume message in RestoreStockReceiveService");
        RestoreStockMsg restoreStockMsg = JSON.parseObject(payload.toString(), RestoreStockMsg.class);
        log.info("consume message in RestoreStockReceiveService: {} success", payload);
        productService.restoreStock(restoreStockMsg);
    }
}
