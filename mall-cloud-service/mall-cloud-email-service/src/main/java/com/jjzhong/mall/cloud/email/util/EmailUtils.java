package com.jjzhong.mall.cloud.email.util;

import com.jjzhong.mall.cloud.email.exception.MallEmailServiceException;
import com.jjzhong.mall.cloud.email.exception.MallEmailServiceExceptionEnum;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * 电子邮件工具类
 */
public class EmailUtils {
    /**
     * 生成随机验证码
     * @param num 验证码长度
     * @return 验证码
     */
    public static String generateVerificationCode(Integer num) {
        return RandomStringUtils.randomAlphanumeric(num);
    }

    /**
     * 校验是否是合法的电子邮件地址
     * @param email 电子邮件地址
     */
    public static void isValidEmailAddress(String email) {
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
        } catch (AddressException e) {
            throw new MallEmailServiceException(MallEmailServiceExceptionEnum.INCORRECT_EMAIL_ADDRESS);
        }
    }

    /**
     * 生成验证码邮件内容
     * @param operation 需要验证码的操作，例如“注册”
     * @param verificationCode 验证码
     * @return 电子邮件内容
     */
    public static String generateEmailContent(String operation, String verificationCode) {
        return "                <head>\n" +
                "                    <base target=\"_blank\" />\n" +
                "                    <style type=\"text/css\">::-webkit-scrollbar{ display: none; }</style>\n" +
                "                    <style id=\"cloudAttachStyle\" type=\"text/css\">#divNeteaseBigAttach, #divNeteaseBigAttach_bak{display:none;}</style>\n" +
                "                    <style id=\"blockquoteStyle\" type=\"text/css\">blockquote{display:none;}</style>\n" +
                "                    <style type=\"text/css\">\n" +
                "                        body{font-size:14px;font-family:arial,verdana,sans-serif;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px}\n" +
                "                        td, input, button, select, body{font-family:Helvetica, 'Microsoft Yahei', verdana}\n" +
                "                        pre {white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;width:95%}\n" +
                "                        th,td{font-family:arial,verdana,sans-serif;line-height:1.666}\n" +
                "                        img{ border:0}\n" +
                "                        header,footer,section,aside,article,nav,hgroup,figure,figcaption{display:block}\n" +
                "                        blockquote{margin-right:0px}\n" +
                "                    </style>\n" +
                "                </head>\n" +
                "                <body tabindex=\"0\" role=\"listitem\">\n" +
                "                <table width=\"700\" border=\"0\" align=\"center\" cellspacing=\"0\" style=\"width:700px;\">\n" +
                "                    <tbody>\n" +
                "                    <tr>\n" +
                "                        <td>\n" +
                "                            <div style=\"width:700px;margin:0 auto;border-bottom:1px solid #ccc;margin-bottom:30px;\">\n" +
                "                                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"700\" height=\"39\" style=\"font:12px Tahoma, Arial, 宋体;\">\n" +
                "                                    <tbody><tr><td width=\"210\"></td></tr></tbody>\n" +
                "                                </table>\n" +
                "                            </div>\n" +
                "                            <div style=\"width:680px;padding:0 10px;margin:0 auto;\">\n" +
                "                                <div style=\"line-height:1.5;font-size:14px;margin-bottom:25px;color:#4d4d4d;\">\n" +
                "                                    <strong style=\"display:block;margin-bottom:15px;\">尊敬的用户：<span style=\"color:#f60;font-size: 16px;\"></span>您好！</strong>\n" +
                "                                    <strong style=\"display:block;margin-bottom:15px;\">您正在进行<span style=\"color: red\">" + operation + "</span>操作，请在验证码输入框中输入：<span style=\"color:#f60;font-size: 24px\">" + verificationCode + "</span>，以完成操作。</strong>\n" +
                "                                </div>\n" +
                "                                <div style=\"margin-bottom:30px;\">\n" +
                "                                    <small style=\"display:block;margin-bottom:20px;font-size:12px;\">\n" +
                "                                        <p style=\"color:#747474;\">\n" +
                "                                            注意：此操作可能会修改您的密码、登录邮箱或绑定手机。如非本人操作，请及时登录并修改密码以保证帐户安全\n" +
                "                                            <br>（工作人员不会向你索取此验证码，请勿泄漏！)\n" +
                "                                        </p>\n" +
                "                                    </small>\n" +
                "                                </div>\n" +
                "                            </div>\n" +
                "                            <div style=\"width:700px;margin:0 auto;\">\n" +
                "                                <div style=\"padding:10px 10px 0;border-top:1px solid #ccc;color:#747474;margin-bottom:20px;line-height:1.3em;font-size:12px;\">\n" +
                "                                    <p>此为系统邮件，请勿回复<br>\n" +
                "                                        请保管好您的邮箱，避免账号被他人盗用\n" +
                "                                    </p>\n" +
                "                                    <p>生鲜商城</p>\n" +
                "                                </div>\n" +
                "                            </div>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    </tbody>\n" +
                "                </table>\n" +
                "                </body>\n";
    }
}
