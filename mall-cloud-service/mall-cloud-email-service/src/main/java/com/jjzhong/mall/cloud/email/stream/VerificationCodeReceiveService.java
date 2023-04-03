package com.jjzhong.mall.cloud.email.stream;

import com.jjzhong.mall.cloud.email.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * Stream 接收消息的服务
 */
@Slf4j
@EnableBinding(VerificationCodeSink.class)
public class VerificationCodeReceiveService {

    @Autowired
    private EmailService emailService;

    /**
     * 接收验证码校验的消息
     * @param payload 消息负载
     */
    @StreamListener(VerificationCodeSink.INPUT)
    public void receiveMessage(@Payload Object payload) {
        log.info("start consume message in VerificationCodeReceiveService");
        String emailAddr = payload.toString();
        log.info("consume message in VerificationCodeReceiveService: {} success", payload);
        emailService.sendVerifyCodeEmail(emailAddr);
    }
}
