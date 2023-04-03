package com.jjzhong.mall.cloud.authority.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.jjzhong.mall.cloud.authority.feign.EmailClient;
import com.jjzhong.mall.cloud.common.constant.CommonConstant;
import com.jjzhong.mall.cloud.common.exception.MallCommonException;
import com.jjzhong.mall.cloud.common.exception.MallCommonExceptionEnum;
import com.jjzhong.mall.cloud.common.model.request.HandleUserReq;
import com.jjzhong.mall.cloud.common.model.vo.CommonUserInfo;
import com.jjzhong.mall.cloud.common.util.TokenParseUtil;
import com.jjzhong.mall.cloud.authority.constant.AuthorityCenterConstant;
import com.jjzhong.mall.cloud.authority.exception.MallAuthorityException;
import com.jjzhong.mall.cloud.authority.exception.MallAuthorityExceptionEnum;
import com.jjzhong.mall.cloud.authority.model.dao.UserMapper;
import com.jjzhong.mall.cloud.common.model.dto.JwtToken;
import com.jjzhong.mall.cloud.authority.model.pojo.User;
import com.jjzhong.mall.cloud.authority.service.UserService;
import com.jjzhong.mall.cloud.authority.model.request.RegisterReq;
import com.jjzhong.mall.cloud.filter.AccessContext;
import com.jjzhong.mall.cloud.sdk.email.request.EmailVerificationCodeCheckReq;
import com.jjzhong.mall.cloud.sdk.email.response.EmailVerificationCodeCheckRes;
import com.jjzhong.mall.cloud.sdk.email.response.status.VerifyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailClient emailClient;

    /***
     * JWT登陆
     * @param handleUserReq 用于处理用户账户的请求
     * @return JWT字符串
     */
    @Override
    public JwtToken loginWithJwt(HandleUserReq handleUserReq) throws Exception {
        User user = checkValidUser(handleUserReq);
        CommonUserInfo commonUserInfo = new CommonUserInfo(user.getId(), user.getUsername(), user.getRole());
        return TokenParseUtil.generateJwt(getPrivateKey(), commonUserInfo, AuthorityCenterConstant.DEFAULT_EXPIRE_DAY);
    }

    /***
     * 普通登陆
     * @param handleUserReq 用于处理用户账户的请求
     * @return JWT字符串
     */
    @Override
    public User login(HandleUserReq handleUserReq) {
        User user = checkValidUser(handleUserReq);
        user.setPassword(null);
        return user;
    }

    /***
     * 管理员登陆
     * @param handleUserReq 用于处理用户账户的请求
     * @return 用户POJO
     */
    @Override
    public User adminLogin(HandleUserReq handleUserReq) {
        User user = checkValidUser(handleUserReq);
        user.setPassword(null);
        if (!user.getRole().equals(CommonConstant.Role.ADMIN.getCode()))
            throw new MallCommonException(MallCommonExceptionEnum.NEED_ADMIN);
        return user;
    }

    /**
     * 注册新用户
     * @param registerReq 注册请求
     */
    @Override
    public void register(RegisterReq registerReq) {
        User result = userMapper.selectByUserName(registerReq.getUserName());
        if (result != null) {
            throw new MallAuthorityException(MallAuthorityExceptionEnum.USER_NAME_EXISTED);
        }
        checkEmailRegistered(registerReq.getEmailAddress());
        // 验证邮箱与验证码是否匹配
        EmailVerificationCodeCheckRes res = emailClient.checkEmailVerificationCodeIsMatch(
                new EmailVerificationCodeCheckReq(registerReq.getEmailAddress(), registerReq.getVerificationCode())
        ).getData();
        if (res == null || !res.getStatus().equals(VerifyStatus.MATCHED))
            throw new MallAuthorityException(MallAuthorityExceptionEnum.INVALID_VERIFICATION_CODE);
        // 创建新用户
        User user = new User();
        user.setUsername(registerReq.getUserName());
        user.setPassword(getMD5PasswordWithSalt(registerReq.getPassword()));
        user.setEmailAddress(registerReq.getEmailAddress());
        int count = userMapper.insertSelective(user);
        if (count == 0) {
            throw new MallCommonException(MallCommonExceptionEnum.INSERT_FAILED);
        }
    }

    /**
     * 更新用户的个性签名
     * @param signature 个性签名
     */
    @Override
    public void updateSignature(String signature) {
        CommonUserInfo commonUserInfo = AccessContext.getCommonUserInfo();
        User user = new User();
        user.setId(commonUserInfo.getId());
        user.setPersonalizedSignature(signature);
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 验证是否是有效的用户
     * @param handleUserReq 用于处理用户账户的请求
     * @return 用户POJO
     */
    private User checkValidUser(HandleUserReq handleUserReq) {
        String md5Password = getMD5PasswordWithSalt(handleUserReq.getPassword());
        User user = userMapper.selectLogin(handleUserReq.getUserName(), md5Password);
        if (user == null) {
            throw new MallAuthorityException(MallAuthorityExceptionEnum.INVALID_USER_NAME_OR_PASSWORD);
        }
        user.setPassword(null);
        return user;
    }

    /**
     * 判断Email地址是否已被注册
     * @param emailAddress Email地址
     */
    @Override
    public void checkEmailRegistered(String emailAddress) {
        User user = userMapper.selectByEmailAddress(emailAddress);
        if (user != null) {
            throw new MallAuthorityException(MallAuthorityExceptionEnum.EMAIL_REGISTERED);
        }
    }

    /***
     * 获得加盐后的MD5密码
     * @param password 未加密的密码
     * @return 加盐后的MD5密码
     */
    private String getMD5PasswordWithSalt(String password) {
        return new String(Base64.getEncoder().encode(MD5.create().digest(password + AuthorityCenterConstant.PWD_SALT)));
    }
    /***
     * 获取私钥
     * @return 私钥
     */
    private PrivateKey getPrivateKey() throws Exception {
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                Base64.getDecoder().decode(AuthorityCenterConstant.JWT_PRIVATE_KEY)
        );
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(priPKCS8);
    }
}
