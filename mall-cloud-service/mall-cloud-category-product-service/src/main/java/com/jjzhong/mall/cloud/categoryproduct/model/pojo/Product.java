package com.jjzhong.mall.cloud.categoryproduct.model.pojo;

import com.jjzhong.mall.cloud.sdk.product.vo.ProductVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * 商品 POJO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Integer id;

    private String name;

    private String image;

    private String detail;

    private Integer categoryId;

    private Integer price;

    private Integer stock;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    public ProductVO toProductVO() {
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(this, productVO);
        return productVO;
    }
}