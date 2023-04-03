package com.jjzhong.mall.cloud.categoryproduct.model.upload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通过 Excel 表格批量上传商品的实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductData {
    private String name;

    private String image;

    private String detail;

    private Integer categoryId;

    private Integer price;

    private Integer stock;

    private Integer status;
}
