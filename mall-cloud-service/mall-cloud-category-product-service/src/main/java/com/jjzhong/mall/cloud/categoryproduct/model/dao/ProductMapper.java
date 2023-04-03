package com.jjzhong.mall.cloud.categoryproduct.model.dao;

import com.jjzhong.mall.cloud.categoryproduct.model.pojo.Product;
import com.jjzhong.mall.cloud.categoryproduct.model.query.ProductListQuery;
import com.jjzhong.mall.cloud.categoryproduct.model.upload.ProductData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product row);

    int insertSelective(Product row);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product row);

    int updateByPrimaryKey(Product row);

    Product selectByName(String name);

    int batchUpdateSellStatus(@Param("ids") Integer[] ids, @Param("sellStatus") Integer sellStatus);

    List<Product> selectListForAdmin();

    List<Product> selectList(@Param("query") ProductListQuery query);
    int updateStock(@Param("productId") Integer productId, @Param("stock") Integer stock);
    int batchInsertUploadedProductData(List<ProductData> products);

    List<Product> selectListByIds(List<Integer> ids);
}