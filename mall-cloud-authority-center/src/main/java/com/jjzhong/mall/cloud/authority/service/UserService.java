package com.jjzhong.mall.cloud.authority.service;

import com.jjzhong.mall.cloud.common.model.request.HandleUserReq;
import com.jjzhong.mall.cloud.common.model.dto.JwtToken;
import com.jjzhong.mall.cloud.authority.model.pojo.User;
import com.jjzhong.mall.cloud.authority.model.request.RegisterReq;

public interface UserService {
    JwtToken loginWithJwt(HandleUserReq handleUserReq) throws Exception;

    User login(HandleUserReq handleUserReq);

    User adminLogin(HandleUserReq handleUserReq);

    void register(RegisterReq registerReq);

    void updateSignature(String signature);

    void checkEmailRegistered(String emailAddress);
}
