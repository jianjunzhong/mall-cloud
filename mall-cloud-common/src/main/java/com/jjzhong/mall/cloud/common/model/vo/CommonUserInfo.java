package com.jjzhong.mall.cloud.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共用户信息类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonUserInfo {
    private Integer id;
    private String username;
    private Integer role;
}
