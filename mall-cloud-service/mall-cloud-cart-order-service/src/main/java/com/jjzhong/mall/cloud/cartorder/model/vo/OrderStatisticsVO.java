package com.jjzhong.mall.cloud.cartorder.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 订单量数据统计 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatisticsVO {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date days;
    private Integer amount;
}
