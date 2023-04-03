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
 * 更新商品请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateProductReq {
    @NotNull(message = "id不能为空")
    private Integer id;

    @NotNull(message = "商品名不能为空")
    private String name;

    @NotNull(message = "商品图片不能为空")
    private String image;

    @Size(min = 5, message = "描述不能少于5字符")
    private String detail;

    @NotNull(message = "商品类别不能为空")
    private Integer categoryId;

    @Min(value = 1000, message = "价格不能小于1000分")
    private Integer price;

    @Max(value = 10000, message = "库存最大为10000")
    private Integer stock;
}
