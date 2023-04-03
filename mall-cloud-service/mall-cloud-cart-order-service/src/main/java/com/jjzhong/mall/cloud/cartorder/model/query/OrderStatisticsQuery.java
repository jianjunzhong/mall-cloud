package com.jjzhong.mall.cloud.cartorder.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 商品订单量统计查询
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatisticsQuery {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
}
