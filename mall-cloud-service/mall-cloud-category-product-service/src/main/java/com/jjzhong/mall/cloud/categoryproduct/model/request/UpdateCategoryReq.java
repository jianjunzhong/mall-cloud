package com.jjzhong.mall.cloud.categoryproduct.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 更新商品类别请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateCategoryReq {
    @NotNull(message = "id不能为空")
    private Integer id;
    @Size(min = 2, max = 5, message = "name长度需要在2到5之间")
    private String name;
    @Max(value = 3, message = "type最大值为3")
    private Integer type;
    private Integer parentId;
    private Integer orderNum;
}
