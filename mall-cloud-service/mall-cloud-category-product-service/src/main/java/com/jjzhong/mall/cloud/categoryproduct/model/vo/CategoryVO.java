package com.jjzhong.mall.cloud.categoryproduct.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 商品类别 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVO {
    private Integer id;

    private String name;

    private Integer type;

    private Integer parentId;

    private Integer orderNum;

    private List<CategoryVO> childCategory;
}
