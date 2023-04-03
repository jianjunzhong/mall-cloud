package com.jjzhong.mall.cloud.email.service.impl;

import com.jjzhong.mall.cloud.email.constant.EmailConstant;
import com.jjzhong.mall.cloud.email.exception.MallEmailServiceException;
import com.jjzhong.mall.cloud.email.exception.MallEmailServiceExceptionEnum;
import com.jjzhong.mall.cloud.email.service.EmailService;
import com.jjzhong.mall.cloud.email.util.EmailUtils;
import com.jjzhong.mall.cloud.sdk.email.request.EmailVerificationCodeCheckReq;
import com.jjzhong.mall.cloud.sdk.email.response.EmailSendRes;
import com.jjzhong.mall.cloud.sdk.email.response.EmailVerificationCodeCheckRes;
import com.jjzhong.mall.cloud.sdk.email.response.status.SendStatus;
import com.jjzhong.mall.cloud.sdk.email.response.status.VerifyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.TimeUnit;

/**
 * 邮件服务
 */
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 发送验证码邮件
     * @param emailAddress 邮件地址
     * @return 响应结果
     */
    @Override
    public EmailSendRes sendVerifyCodeEmail(String emailAddress) {
        EmailSendRes res;
        try {
            EmailUtils.isValidEmailAddress(emailAddress);
            String verificationCode = EmailUtils.generateVerificationCode(EmailConstant.VERIFICATION_CODE_LENGTH);
            saveVerifyCodeToRedis(emailAddress, verificationCode);
            sendMimeEmailMessage(
                    EmailConstant.EMAIL_FROM,
                    emailAddress,
                    EmailConstant.EMAIL_SUBJECT,
                    EmailUtils.generateEmailContent("注册新用户", verificationCode)
            );
            res = new EmailSendRes(null, SendStatus.SUCCESS);
        } catch (MallEmailServiceException | MessagingException e) {
            res = new EmailSendRes(e.getMessage(), SendStatus.ERROR);
        }
        return res;
    }

    /**
     * 检查验证码与邮箱是否匹配
     * @param request 请求
     * @return 验证结果
     */
    @Override
    public EmailVerificationCodeCheckRes checkIsMatch(EmailVerificationCodeCheckReq request) {
        VerifyStatus status;
        String savedVerificationCode = redisTemplate.boundValueOps(request.getEmailAddress()).get();
        if (savedVerificationCode == null) {
            status = VerifyStatus.EMAIL_NOT_SEND;
        } else if (!savedVerificationCode.equals(request.getVerificationCode())) {
            status = VerifyStatus.NOT_MATCH;
        } else {
            status = VerifyStatus.MATCHED;
        }
        return new EmailVerificationCodeCheckRes(status.getMsg(), status);
    }

    /**
     * 发送简单的（只包含文本的） Email
     * @param from 来源 Email
     * @param to 目的 Email
     * @param subject 主题
     * @param text 内容
     */
    private void sendSimpleEmailMessage(String from, String to, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        mailSender.send(simpleMailMessage);
    }

    /**
     * 发送复杂的（可能带有 html）的邮件
     * @param from 来源 Email
     * @param to 目的 Email
     * @param subject 主题
     * @param text 内容
     * @throws MessagingException 创建邮件失败抛出的异常
     */
    private void sendMimeEmailMessage(String from, String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        mailSender.send(message);
    }

    /**
     * 将验证码保存在 redis 中
     * @param emailAddress 邮件地址
     * @param verificationCode 验证码
     */
    private void saveVerifyCodeToRedis(String emailAddress, String verificationCode) {
        BoundValueOperations<String, String> boundValueOps = redisTemplate.boundValueOps(emailAddress);
        if (boundValueOps.get() == null) {
            boundValueOps.set(verificationCode, 60, TimeUnit.SECONDS);
        } else {
            throw new MallEmailServiceException(MallEmailServiceExceptionEnum.EMAIL_SENT);
        }
    }
}
