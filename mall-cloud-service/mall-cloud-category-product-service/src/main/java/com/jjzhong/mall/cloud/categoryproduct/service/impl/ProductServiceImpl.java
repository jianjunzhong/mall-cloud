package com.jjzhong.mall.cloud.categoryproduct.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jjzhong.mall.cloud.categoryproduct.constant.CategoryProductConstant;
import com.jjzhong.mall.cloud.categoryproduct.constant.ProductListOrderByEnum;
import com.jjzhong.mall.cloud.categoryproduct.exception.MallCategoryProductServiceException;
import com.jjzhong.mall.cloud.categoryproduct.exception.MallCategoryProductServiceExceptionEnum;
import com.jjzhong.mall.cloud.categoryproduct.listener.ProductDataListener;
import com.jjzhong.mall.cloud.categoryproduct.model.dao.ProductMapper;
import com.jjzhong.mall.cloud.categoryproduct.model.pojo.Product;
import com.jjzhong.mall.cloud.categoryproduct.model.query.ProductListQuery;
import com.jjzhong.mall.cloud.categoryproduct.model.request.AddProductReq;
import com.jjzhong.mall.cloud.categoryproduct.model.request.QueryProductReq;
import com.jjzhong.mall.cloud.categoryproduct.model.request.UpdateProductReq;
import com.jjzhong.mall.cloud.categoryproduct.model.upload.ProductData;
import com.jjzhong.mall.cloud.categoryproduct.service.CategoryService;
import com.jjzhong.mall.cloud.categoryproduct.service.ProductService;
import com.jjzhong.mall.cloud.common.exception.MallCommonException;
import com.jjzhong.mall.cloud.common.exception.MallCommonExceptionEnum;
import com.jjzhong.mall.cloud.sdk.product.message.RestoreStockMsg;
import com.jjzhong.mall.cloud.sdk.product.request.RestoreStockReq;
import com.jjzhong.mall.cloud.categoryproduct.constant.ProductSaleStatusEnum;
import com.jjzhong.mall.cloud.sdk.product.dto.DeductStockDTO;
import com.jjzhong.mall.cloud.sdk.product.request.DeductStockReq;
import com.jjzhong.mall.cloud.sdk.product.vo.ProductVO;
import com.jjzhong.mall.cloud.sdk.product.request.UpdateStockReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 商品服务
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 增加商品
     * @param addProductReq 增加商品请求
     */
    @Override
    public void add(AddProductReq addProductReq) {
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if (productOld != null) {
            throw new MallCategoryProductServiceException(MallCategoryProductServiceExceptionEnum.NAME_EXISTED);
        }
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq, product);
        int cnt = productMapper.insertSelective(product);
        if (cnt == 0) {
            throw new MallCommonException(MallCommonExceptionEnum.INSERT_FAILED);
        }
    }

    /**
     * 管理员后台更新商品，并将更新后的商品缓存到 redis 中
     * @param updateProductReq 更新商品请求
     */
    @Override
    public void update(UpdateProductReq updateProductReq) {
        Product productOld = productMapper.selectByName(updateProductReq.getName());
        if (productOld != null && !productOld.getId().equals(updateProductReq.getId())) {
            throw new MallCategoryProductServiceException(MallCategoryProductServiceExceptionEnum.NAME_EXISTED);
        }
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq, product);
        int cnt = productMapper.updateByPrimaryKeySelective(product);
        if (cnt == 0) {
            throw new MallCommonException(MallCommonExceptionEnum.UPDATE_FAILED);
        }
        List<Product> t = Stream.of(product).collect(Collectors.toList());
        cacheProductsToRedis(t);
    }

    /**
     * 删除商品
     * @param productId 商品 id
     */
    @Override
    public void delete(Integer productId) {
        int cnt = productMapper.deleteByPrimaryKey(productId);
        if (cnt == 0) {
            throw new MallCommonException(MallCommonExceptionEnum.DELETE_FAILED);
        }
        // 从 redis 中删除
        redisTemplate.opsForHash().delete(CategoryProductConstant.MALL_CLOUD_PRODUCT_DICT_KEY, productId.toString());
    }

    /**
     * 批量更新商品上架状态
     * @param ids 商品 id 数组
     * @param saleStatus 上架状态
     */
    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer saleStatus) {
        ProductSaleStatusEnum.of(saleStatus);
        productMapper.batchUpdateSellStatus(ids, saleStatus);
    }

    /**
     * 查询商品列表返回给管理员后台
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页后的商品
     */
    @Override
    public PageInfo<Product> listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> list = productMapper.selectListForAdmin();
        return new PageInfo<>(list);
    }

    /**
     * 扣减库存，并将扣减库存后的 ProductVO 保存在 redis 中
     * @param deductStockReq 商品库存扣减请求
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deductStock(DeductStockReq deductStockReq) {
        List<DeductStockDTO> dtos = deductStockReq.getDtos();
        // 若有扣减数量小于或等于 0 的扣减请求，则抛出异常
        if (dtos.stream().anyMatch(d -> d.getCount() <= 0))
            throw new MallCategoryProductServiceException(MallCategoryProductServiceExceptionEnum.INVALID_STOCK_DEDUCT_COUNT);
        // 从数据库中查找商品
        List<Integer> productIds = dtos.stream()
                .map(d -> d.getProductId())
                .collect(Collectors.toList());
        List<Product> products = productMapper.selectListByIds(productIds);
        if (products.size() != dtos.size()) {
            throw new MallCategoryProductServiceException(MallCategoryProductServiceExceptionEnum.PRODUCT_NOT_FOUND);
        }
        // 建立商品 id 到 DTO 的映射
        Map<Integer, DeductStockDTO> deductStockMap = dtos.stream()
                .collect(Collectors.toMap(DeductStockDTO::getProductId, Function.identity()));
        products.forEach(product -> {
            Integer currentStock = product.getStock();
            Integer needStock = deductStockMap.get(product.getId()).getCount();
            // 判断是否有商品库存小于扣减数量，若有则抛出异常
            if (currentStock < needStock)
                throw new MallCategoryProductServiceException(MallCategoryProductServiceExceptionEnum.STOCK_NOT_ENOUGH);
            // 设置并更新库存
            product.setStock(currentStock - needStock);
            productMapper.updateStock(product.getId(), product.getStock());
        });
        // 将更新库存的商品缓存到 redis 中
        cacheProductsToRedis(products);
    }

    /**
     * 从数据库中查询用户发来的商品查询请求，并缓存到 Redis 中
     * @param queryProductReq 查询请求
     * @return 商品分页数据
     */
    @Override
    public PageInfo<ProductVO> queryFromDBAndCachedToRedis(QueryProductReq queryProductReq) {
        ProductListQuery productListQuery = new ProductListQuery();
        if (queryProductReq.getCategoryId() != null) {
            List<Integer> idList = new ArrayList<>();
            idList.add(queryProductReq.getCategoryId());
            categoryService.getChildCategoryIds(queryProductReq.getCategoryId(), idList);
            productListQuery.setCategoryIds(idList);
        }
        if (StringUtils.hasText(queryProductReq.getKeyword())) {
            String keyword = "%" + queryProductReq.getKeyword() + "%";
            productListQuery.setKeyword(keyword);
        }
        if (queryProductReq.getOrderBy() != null) {
            String orderBy = queryProductReq.getOrderBy();
            PageHelper.startPage(queryProductReq.getPageNum(), queryProductReq.getPageSize(), ProductListOrderByEnum.of(orderBy).getOrderBy());
        }
        List<Product> list = productMapper.selectList(productListQuery);
        List<ProductVO> productVOs = cacheProductsToRedis(list);
        return new PageInfo<>(productVOs);
    }

    /**
     * 从数据库中查找并缓存到 redis 中
     * @param ids 商品主键 id 列表
     * @return ProductVO 列表
     */
    @Override
    public List<ProductVO> queryFromDBAndCachedToRedis(List<Integer> ids) {
        List<Product> list = productMapper.selectListByIds(ids);
        List<ProductVO> productVOs = null;
        if (CollectionUtils.isNotEmpty(list)) {
            productVOs = cacheProductsToRedis(list);
            if (list.size() != ids.size()) {
                throw new MallCategoryProductServiceException(MallCategoryProductServiceExceptionEnum.PRODUCT_NOT_FOUND);
            }
        }
        return productVOs;
    }

    /**
     * 用户查询商品详情
     * @param id 商品 id
     * @return 商品详情
     */
    @Override
    public ProductVO detail(Integer id) {
        List<Integer> t = Stream.of(id).collect(Collectors.toList());
        ProductVO productVO = queryProductVOFromDBAndRedis(t).get(0);
        return productVO;
    }

    /**
     * 从数据库和 redis 中查询商品信息，并将数据库中查询到的数据缓存到 redis 中
     * @param ids 商品主键 id
     * @return ProductVO 列表
     */
    @Override
    public List<ProductVO> queryProductVOFromDBAndRedis(List<Integer> ids) {
        // 将 id 转换为需要从 redis 查询的字符串类型
        List<Object> idsStr = ids.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
        // 从 redis 查询非空字符串类型的商品
        List<Object> cachedProductVOStr = redisTemplate.opsForHash()
                .multiGet(CategoryProductConstant.MALL_CLOUD_PRODUCT_DICT_KEY, idsStr)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        // 若不为空，分两种情况讨论
        if (CollectionUtils.isNotEmpty(cachedProductVOStr)) {
            // 将从 redis 查询到的商品反序列化为实体类
            List<ProductVO> left = parseCachedProductVO(cachedProductVOStr);
            // 1. 若全部从 redis 中查询到，则全部返回
            if (left.size() == ids.size())
                return left;
            // 2. 若只从 redis 中查询到一部分（left），未查询到的部分（right）从数据库中查询
            else {
                // 获取 right 部分的 id
                List<Integer> rightIds = CollectionUtils.subtract(
                                ids,
                                left.stream()
                                        .map(ProductVO::getId)
                                        .collect(Collectors.toList())
                        ).stream()
                        .collect(Collectors.toList());
                // 从数据库中查询
                List<ProductVO> right = queryFromDBAndCachedToRedis(rightIds);
                // 合并
                List<ProductVO> union = CollectionUtils.union(left, right).stream().collect(Collectors.toList());
                return union;
            }
        } else
            // 为空，直接从数据库中查询并缓存
            return queryFromDBAndCachedToRedis(ids);
    }

    /**
     * 将 Excel 中的商品信息批量导入到数据库中
     * @param file 上传的文件
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addProductByExcel(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), ProductData.class, new ProductDataListener(productMapper)).sheet().doRead();
        } catch (IOException e) {
            throw new MallCommonException(MallCommonExceptionEnum.INSERT_FAILED);
        }
    }

    /**
     * 管理员后台更新库存
     * @param updateStockReq 更新库存请求
     */
    @Override
    public void updateStock(UpdateStockReq updateStockReq) {
        int cnt = productMapper.updateStock(updateStockReq.getProductId(), updateStockReq.getStock());
        if (cnt <= 0) {
            throw new MallCommonException(MallCommonExceptionEnum.UPDATE_FAILED);
        }
    }

    /**
     * 订单取消后，恢复商品库存
     * @param restoreStockReq 恢复库存请求
     */
    @Override
    public void restoreStock(RestoreStockReq restoreStockReq) {
        Product product = productMapper.selectByPrimaryKey(restoreStockReq.getProductId());
        int cnt = productMapper.updateStock(restoreStockReq.getProductId(), product.getStock() + restoreStockReq.getQuantity());
        if (cnt <= 0) {
            throw new MallCommonException(MallCommonExceptionEnum.UPDATE_FAILED);
        }
    }

    /**
     * 恢复库存
     * @param restoreStockMsg 消息队列中的恢复库存消息
     */
    @Override
    public void restoreStock(RestoreStockMsg restoreStockMsg) {
        RestoreStockReq restoreStockReq = new RestoreStockReq();
        BeanUtils.copyProperties(restoreStockMsg, restoreStockReq);
        restoreStock(restoreStockReq);
    }

    /**
     * 将缓存中的数据反序列化为 VO 对象
     * @param cachedProductVO 缓存的 VO 对象字符串
     * @return 反序列化之后的对象
     */
    private List<ProductVO> parseCachedProductVO(List<Object> cachedProductVO) {
        return cachedProductVO.stream()
                .map(p -> JSON.parseObject((String) p, ProductVO.class))
                .collect(Collectors.toList());
    }

    /**
     * 将 Product 转换为 ProductVO 并缓存到 redis
     * @param products 商品列表
     * @return ProductVO 列表
     */
    private List<ProductVO> cacheProductsToRedis(List<Product> products) {
        List<ProductVO> productVOList = products.stream()
                .map(Product::toProductVO)
                .collect(Collectors.toList());
        return cacheProductVOsToRedis(productVOList);
    }

    /**
     * 将 ProductVO 缓存到 redis
     * @param products 商品列表
     * @return ProductVO 列表
     */
    private List<ProductVO> cacheProductVOsToRedis(List<ProductVO> productVOList) {
        Map<String, String> id2JsonObject = new HashMap<>(productVOList.size());
        productVOList.forEach(
                productVO -> id2JsonObject.put(
                        productVO.getId().toString(),
                        JSON.toJSONString(productVO)
                )
        );
        redisTemplate.opsForHash().putAll(
                CategoryProductConstant.MALL_CLOUD_PRODUCT_DICT_KEY, id2JsonObject
        );
        return productVOList;
    }
}
