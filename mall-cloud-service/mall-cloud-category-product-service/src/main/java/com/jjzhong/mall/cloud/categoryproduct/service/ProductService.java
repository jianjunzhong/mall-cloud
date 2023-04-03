package com.jjzhong.mall.cloud.categoryproduct.service;

import com.github.pagehelper.PageInfo;
import com.jjzhong.mall.cloud.categoryproduct.model.pojo.Product;
import com.jjzhong.mall.cloud.categoryproduct.model.request.AddProductReq;
import com.jjzhong.mall.cloud.categoryproduct.model.request.QueryProductReq;
import com.jjzhong.mall.cloud.categoryproduct.model.request.UpdateProductReq;
import com.jjzhong.mall.cloud.sdk.product.message.RestoreStockMsg;
import com.jjzhong.mall.cloud.sdk.product.request.RestoreStockReq;
import com.jjzhong.mall.cloud.sdk.product.request.DeductStockReq;
import com.jjzhong.mall.cloud.sdk.product.vo.ProductVO;
import com.jjzhong.mall.cloud.sdk.product.request.UpdateStockReq;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    void add(AddProductReq addProductReq);

    void update(UpdateProductReq updateProductReq);

    void delete(Integer productId);

    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    PageInfo<Product> listForAdmin(Integer pageNum, Integer pageSize);

    @Transactional(rollbackFor = Exception.class)
    void deductStock(DeductStockReq deductStockReq);

    PageInfo<ProductVO> queryFromDBAndCachedToRedis(QueryProductReq queryProductReq);

    List<ProductVO> queryFromDBAndCachedToRedis(List<Integer> ids);

    ProductVO detail(Integer id);

    List<ProductVO> queryProductVOFromDBAndRedis(List<Integer> ids);

    void addProductByExcel(MultipartFile file);

    void updateStock(UpdateStockReq updateStockReq);

    void restoreStock(RestoreStockReq restoreStockReq);

    void restoreStock(RestoreStockMsg restoreStockMsg);
}
