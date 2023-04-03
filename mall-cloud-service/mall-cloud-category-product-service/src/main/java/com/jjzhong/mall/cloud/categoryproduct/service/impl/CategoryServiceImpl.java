package com.jjzhong.mall.cloud.categoryproduct.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jjzhong.mall.cloud.categoryproduct.exception.MallCategoryProductServiceException;
import com.jjzhong.mall.cloud.categoryproduct.exception.MallCategoryProductServiceExceptionEnum;
import com.jjzhong.mall.cloud.categoryproduct.model.dao.CategoryMapper;
import com.jjzhong.mall.cloud.categoryproduct.model.pojo.Category;
import com.jjzhong.mall.cloud.categoryproduct.model.request.AddCategoryReq;
import com.jjzhong.mall.cloud.categoryproduct.model.request.UpdateCategoryReq;
import com.jjzhong.mall.cloud.categoryproduct.model.vo.CategoryVO;
import com.jjzhong.mall.cloud.categoryproduct.service.CategoryService;
import com.jjzhong.mall.cloud.common.exception.MallCommonException;
import com.jjzhong.mall.cloud.common.exception.MallCommonExceptionEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品类别服务
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(AddCategoryReq addCategoryReq) {
        Category categoryOld = categoryMapper.selectByName(addCategoryReq.getName());
        if (categoryOld != null) {
            throw new MallCategoryProductServiceException(MallCategoryProductServiceExceptionEnum.NAME_EXISTED);
        }
        Category category = new Category();
        BeanUtils.copyProperties(addCategoryReq, category);
        int cnt = categoryMapper.insertSelective(category);
        if (cnt == 0) {
            throw new MallCommonException(MallCommonExceptionEnum.INSERT_FAILED);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(UpdateCategoryReq updateCategoryReq) {
        Category categoryOld1 = categoryMapper.selectByPrimaryKey(updateCategoryReq.getId());
        if (categoryOld1 == null) {
            throw new MallCommonException(MallCommonExceptionEnum.REQUEST_PARAM_ERROR);
        }
        Category categoryOld2 = categoryMapper.selectByName(updateCategoryReq.getName());
        if (categoryOld2 != null) {
            throw new MallCategoryProductServiceException(MallCategoryProductServiceExceptionEnum.NAME_EXISTED);
        }
        Category category = new Category();
        BeanUtils.copyProperties(updateCategoryReq, category);
        int cnt = categoryMapper.updateByPrimaryKeySelective(category);
        if (cnt == 0) {
            throw new MallCommonException(MallCommonExceptionEnum.UPDATE_FAILED);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer categoryId) {
        Category categoryOld = categoryMapper.selectByPrimaryKey(categoryId);
        if (categoryOld == null) {
            throw new MallCommonException(MallCommonExceptionEnum.DELETE_FAILED);
        }
        int cnt = categoryMapper.deleteByPrimaryKey(categoryId);
        if (cnt == 0) {
            throw new MallCommonException(MallCommonExceptionEnum.DELETE_FAILED);
        }
    }
    @Override
    public PageInfo<Category> listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, "type, order_num");
        List<Category> categoryList = categoryMapper.selectList();
        return new PageInfo<>(categoryList);
    }

    @Override
    @Cacheable(value = "listForCustomer")
    public List<CategoryVO> listForCustomer() {
        return recursivelyGetCustomerList(0);
    }

    @Override
    public void getChildCategoryIds(Integer parentId, List<Integer> ids) {
        List<Category> categories = categoryMapper.selectByParentId(parentId);
        if (!CollectionUtils.isEmpty(categories)) {
            for (Category category : categories) {
                ids.add(category.getId());
                getChildCategoryIds(category.getId(), ids);
            }
        }
    }

    private List<CategoryVO> recursivelyGetCustomerList(Integer parentId) {
        List<Category> categories = categoryMapper.selectByParentId(parentId);
        if (!CollectionUtils.isEmpty(categories)) {
            List<CategoryVO> categoryVOs = new ArrayList<>();
            for (Category category : categories) {
                CategoryVO categoryVO = new CategoryVO();
                BeanUtils.copyProperties(category, categoryVO);
                categoryVO.setChildCategory(recursivelyGetCustomerList(category.getId()));
                categoryVOs.add(categoryVO);
            }
            return categoryVOs;
        }
        return new ArrayList<>();
    }
}
