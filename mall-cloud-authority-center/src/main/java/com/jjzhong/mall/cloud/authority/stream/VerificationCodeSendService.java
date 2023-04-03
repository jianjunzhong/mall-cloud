package com.jjzhong.mall.cloud.authority.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

/**
 * 向消息队列中发送消息，从而实现异步发送邮箱验证码的 Stream 服务
 */
@Slf4j
@EnableBinding(VerificationCodeSource.class)
public class VerificationCodeSendService {
    @Autowired
    private VerificationCodeSource verificationCodeSource;

    public void sendMessage(String emailAddr) {
        log.info("send message in VerificationCodeSendService: {}", emailAddr);
        verificationCodeSource.restoreStockOutput().send(
                MessageBuilder.withPayload(emailAddr).build()
        );
    }
}
