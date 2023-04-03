package com.jjzhong.mall.cloud.common.util;

import com.alibaba.fastjson.JSON;
import com.jjzhong.mall.cloud.common.constant.CommonConstant;
import com.jjzhong.mall.cloud.common.model.dto.JwtToken;
import com.jjzhong.mall.cloud.common.model.vo.CommonUserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * 用于生成JWT和解析JWT中用户信息的工具类
 */
public class TokenParseUtil {
    /***
     * 生成JWT
     * @param privateKey 私钥
     * @param commonUserInfo 包含username, userId和userRole的实体类
     * @param expire 过期时间（以天为单位）
     * @return JWT字符串
     */
    public static JwtToken generateJwt(PrivateKey privateKey, CommonUserInfo commonUserInfo, int expire) {
        // 计算超时时间
        ZonedDateTime zdt = LocalDateTime.now().plus(expire, ChronoUnit.DAYS).atZone(ZoneId.systemDefault());
        Date expireDate = Date.from(zdt.toInstant());
        return new JwtToken(
                Jwts.builder()
                        .claim(CommonConstant.USER_INFO, JSON.toJSONString(commonUserInfo))
                        .setId(UUID.randomUUID().toString())
                        .setExpiration(expireDate)
                        .signWith(privateKey, SignatureAlgorithm.RS256)
                        .compact()
        );
    }

    /**
     * 从JWT中解析用户信息
     * @param token JWT Token 字符串
     * @return 若为空，返回null
     * @throws Exception 从JWT中解析用户信息时出错
     */
    public static CommonUserInfo parseCommonUserInfoFromToken(String token) throws Exception {
        if (token == null) return null;
        Jws<Claims> claimsJws = parseToken(token, getPublicKey());
        Claims body = claimsJws.getBody();
        if (body.getExpiration().before(Calendar.getInstance().getTime())) {
            return null;
        }
        return JSON.parseObject(body.get(CommonConstant.USER_INFO).toString(), CommonUserInfo.class);
    }

    private static Jws<Claims> parseToken(String token, PublicKey publicKey) {
        return Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
    }

    private static PublicKey getPublicKey() throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(CommonConstant.JWT_PUBLIC_KEY));
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }
}
