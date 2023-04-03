package com.jjzhong.mall.cloud.categoryproduct.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 增加商品请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddProductReq {

    @NotNull(message = "name不能为空")
    private String name;

    @NotNull(message = "请上传图片")
    private String image;

    @NotNull(message = "detail不能为空")
    @Size(min = 5, message = "描述不能少于5字符")
    private String detail;

    @NotNull(message = "categoryId不能为空")
    private Integer categoryId;

    @NotNull(message = "price不能为空")
    @Min(value = 1000, message = "价格不能小于1000分")
    private Integer price;

    @NotNull(message = "stock不能为空")
    @Max(10000)
    private Integer stock;
}
