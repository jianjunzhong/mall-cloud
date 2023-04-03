package com.jjzhong.mall.cloud.categoryproduct.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 查询商品请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QueryProductReq {
    private String orderBy;
    private Integer categoryId;
    private String keyword;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
