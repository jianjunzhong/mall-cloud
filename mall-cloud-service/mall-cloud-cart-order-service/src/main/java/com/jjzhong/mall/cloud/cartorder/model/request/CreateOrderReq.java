package com.jjzhong.mall.cloud.cartorder.model.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 创建订单请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateOrderReq {
    @NotNull
    private String receiverName;

    @NotNull
    private String receiverMobile;

    @NotNull
    private String receiverAddress;
}
