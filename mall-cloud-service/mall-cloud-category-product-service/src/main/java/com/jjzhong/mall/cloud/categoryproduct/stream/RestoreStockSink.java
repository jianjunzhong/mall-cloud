package com.jjzhong.mall.cloud.categoryproduct.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * Stream 自定义消费者 Sink
 */
public interface RestoreStockSink {
    String INPUT = "restoreStockInput";
    @Input(RestoreStockSink.INPUT)
    SubscribableChannel restoreStockInput();
}
