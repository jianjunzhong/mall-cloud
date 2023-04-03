package com.jjzhong.mall.cloud.cartorder.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface RestoreStockSource {
    String OUTPUT = "restoreStockOutput";

    @Output(RestoreStockSource.OUTPUT)
    MessageChannel restoreStockOutput();
}
