package com.jjzhong.mall.cloud.email.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 电子邮件服务常量
 */
@Component
public class EmailConstant {
    /** 邮件主题 */
    public static final String EMAIL_SUBJECT = "生鲜商城验证码";
    /** 验证码长度 */
    public static final Integer VERIFICATION_CODE_LENGTH = 5;
    /** 邮件来源 */
    public static String EMAIL_FROM;
    @Value("${spring.mail.username}")
    public void setEmailFrom(String emailFrom) {
        EMAIL_FROM = emailFrom;
    }

}
