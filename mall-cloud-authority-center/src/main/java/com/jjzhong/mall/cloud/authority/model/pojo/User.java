package com.jjzhong.mall.cloud.authority.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;

    private String username;

    /** 密码（加盐后MD5编码，再进行base64编码） */
    private String password;

    private String emailAddress;

    /** 个性签名 */
    private String personalizedSignature;

    /** 角色 */
    private Integer role;

    private Date createTime;

    private Date updateTime;
}