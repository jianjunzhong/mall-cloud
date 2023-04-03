package com.jjzhong.mall.cloud.categoryproduct.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 商品类别 POJO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Integer id;

    private String name;

    private Integer type;

    private Integer parentId;

    private Integer orderNum;

    private Date createTime;

    private Date updateTime;
}