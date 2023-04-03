package com.jjzhong.mall.cloud.categoryproduct.model.dao;

import com.jjzhong.mall.cloud.categoryproduct.model.pojo.Category;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category row);

    int insertSelective(Category row);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category row);

    int updateByPrimaryKey(Category row);

    Category selectByName(String name);

    List<Category> selectList();

    List<Category> selectByParentId(Integer parentId);
}