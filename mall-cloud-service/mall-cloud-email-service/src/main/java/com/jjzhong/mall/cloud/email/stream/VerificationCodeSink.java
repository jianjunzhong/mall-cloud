package com.jjzhong.mall.cloud.email.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * Stream 自定义消费者 Sink
 */
public interface VerificationCodeSink {
    String INPUT = "verificationCodeInput";
    @Input(VerificationCodeSink.INPUT)
    SubscribableChannel verificationCOdeInput();
}
