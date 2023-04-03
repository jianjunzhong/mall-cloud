package com.jjzhong.mall.cloud.authority.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 自定义 Source
 */
public interface VerificationCodeSource {
    String OUTPUT = "verificationCodeOutput";

    @Output(VerificationCodeSource.OUTPUT)
    MessageChannel restoreStockOutput();
}
