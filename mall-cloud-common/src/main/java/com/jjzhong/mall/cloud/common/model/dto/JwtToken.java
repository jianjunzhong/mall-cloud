package com.jjzhong.mall.cloud.common.model.dto;

import com.jjzhong.mall.cloud.common.constant.CommonConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {
    private final String name = CommonConstant.JWT_TOKEN;
    private String token;
}
