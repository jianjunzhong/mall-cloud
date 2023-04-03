package com.jjzhong.mall.cloud.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * 公共常量
 */
@Component
public class CommonConstant {
    /** jwt 公钥 */
    public static final String JWT_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnV+iGlE1e8Z825G+ChIwRJ2H2jOMCBu" +
            "HV7BPrUE8dAGjqAlRtCaxMyJw7NV9NIUl/rY7RWBUQwelkGmGuQomnUAFIgN9f8UxSC6G935lo1ZoBVJWYmfs5ToXLz+fQugmqHZvF+Vc5l" +
            "UEo1YapeiaymkOxDORMGjzQBoxoBt316IAwNEPIvcV+F6T+WNFJX/p5Xj48Z1rtmbOQ8ffF+pEWKZGsYg/9b+pKiqFJtuyHqwj/9oxFBE98" +
            "MCu5RfK6M7Ff9/1dyNen1HKjI7Awj8ZnSceVUldcXEdnP89YagevbhtSl/+CvCsKwHq5+ZLkcuONSxE4dIFWTjxA92wJjYf9wIDAQAB";
    public static final String JWT_TOKEN = "jwt_token";
    public static final String USER_INFO = "user_info";
    public static final String MALL_USER = "mall_user";

    @Getter
    @AllArgsConstructor
    public enum Role {
        CUSTOMER(1),
        ADMIN(2);
        private final Integer code;
    }
}
