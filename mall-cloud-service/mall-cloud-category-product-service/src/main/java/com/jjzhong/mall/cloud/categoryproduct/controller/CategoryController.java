package com.jjzhong.mall.cloud.categoryproduct.controller;

import com.github.pagehelper.PageInfo;
import com.jjzhong.mall.cloud.categoryproduct.model.pojo.Category;
import com.jjzhong.mall.cloud.categoryproduct.model.request.AddCategoryReq;
import com.jjzhong.mall.cloud.categoryproduct.model.request.UpdateCategoryReq;
import com.jjzhong.mall.cloud.categoryproduct.model.vo.CategoryVO;
import com.jjzhong.mall.cloud.categoryproduct.service.CategoryService;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "商品目录接口")
@RestController
@Validated
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/admin/category/add")
    @Operation(description = "增加目录")
    public CommonResponse<Object> addCategory(@Valid @RequestBody AddCategoryReq addCategoryReq) {
        categoryService.add(addCategoryReq);
        return CommonResponse.success();
    }

    @PostMapping("/admin/category/update")
    @Operation(description = "更新目录")
    public CommonResponse<Object> updateCategory(@Valid @RequestBody UpdateCategoryReq updateCategoryReq) {
        categoryService.update(updateCategoryReq);
        return CommonResponse.success();
    }

    @PostMapping("/admin/category/delete")
    @Operation(description = "删除目录")
    public CommonResponse<Object> deleteCategory(@RequestParam Integer id) {
        categoryService.delete(id);
        return CommonResponse.success();
    }

    @GetMapping("/admin/category/list")
    @Operation(description = "返回目录列表（管理员）")
    public CommonResponse<PageInfo<Category>> categoryListForAdmin(Integer pageNum, Integer pageSize) {
        return CommonResponse.success(categoryService.listForAdmin(pageNum, pageSize));
    }

    @GetMapping("/category/list")
    @Operation(description = "返回目录列表（普通用户）")
    public List<CategoryVO> categoryListForCustomer() {
        return categoryService.listForCustomer();
    }
}
