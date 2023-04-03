package com.jjzhong.mall.cloud.filter;

import com.jjzhong.mall.cloud.common.constant.CommonConstant;
import com.jjzhong.mall.cloud.common.exception.MallCommonException;
import com.jjzhong.mall.cloud.common.exception.MallCommonExceptionEnum;
import com.jjzhong.mall.cloud.common.model.vo.CommonUserInfo;
import com.jjzhong.mall.cloud.common.util.TokenParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户信息拦截器
 */
@Slf4j
public class CommonUserInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 若不是预检请求，则解析请求头 jwt_token 中的用户信息
        if (!request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            String token = request.getHeader(CommonConstant.JWT_TOKEN);
            if (token != null) {
                CommonUserInfo commonUserInfo;
                try {
                    commonUserInfo = TokenParseUtil.parseCommonUserInfoFromToken(token);
                } catch (Exception e) {
                    throw new MallCommonException(MallCommonExceptionEnum.NEED_LOGIN);
                }
                if (commonUserInfo == null)
                    throw new MallCommonException(MallCommonExceptionEnum.UNKNOWN_ERROR);
                AccessContext.setCommonUserInfo(commonUserInfo);
            } else
                throw new MallCommonException(MallCommonExceptionEnum.UNKNOWN_ERROR);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 为防止内存泄露，需要将 ThreadLocal 中的用户信息删除
        if (AccessContext.getCommonUserInfo() != null)
            AccessContext.clearCommonUserInfo();
    }
}
