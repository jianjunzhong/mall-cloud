package com.jjzhong.mall.cloud.filter;

import com.jjzhong.mall.cloud.common.model.vo.CommonUserInfo;

/**
 * 获取请求的上下文（用户信息）
 */
public class AccessContext {
    private static final ThreadLocal<CommonUserInfo> commonUserInfo = new ThreadLocal<>();
    public static CommonUserInfo getCommonUserInfo() {
        return commonUserInfo.get();
    }

    public static void setCommonUserInfo(CommonUserInfo commonUserInfo_) {
        commonUserInfo.set(commonUserInfo_);
    }

    public static void clearCommonUserInfo() {
        commonUserInfo.remove();
    }
}
