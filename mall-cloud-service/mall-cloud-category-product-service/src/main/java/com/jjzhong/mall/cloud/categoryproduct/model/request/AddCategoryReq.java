package com.jjzhong.mall.cloud.categoryproduct.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 增加商品类别请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCategoryReq {
    @NotNull(message = "name不能为空")
    @Size(min = 2, max = 5, message = "name长度需要在2到5之间")
    private String name;
    @NotNull(message = "type不能为空")
    @Max(value = 3, message = "type最大值为3")
    private Integer type;
    @NotNull(message = "parentId不能为空")
    private Integer parentId;
    @NotNull(message = "orderNum不能为空")
    private Integer orderNum;

    @Override
    public String toString() {
        return "AddCategoryReq{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", parentId=" + parentId +
                ", orderNum=" + orderNum +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
