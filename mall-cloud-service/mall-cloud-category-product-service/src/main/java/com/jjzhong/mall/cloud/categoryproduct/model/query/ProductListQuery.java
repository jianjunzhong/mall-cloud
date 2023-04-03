package com.jjzhong.mall.cloud.categoryproduct.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 数据库中商品查询
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListQuery {
    /** 商品类别 id */
    private List<Integer> categoryIds;
    /** 关键词 */
    private String keyword;
}
