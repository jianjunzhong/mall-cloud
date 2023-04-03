package com.jjzhong.mall.cloud.categoryproduct.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.jjzhong.mall.cloud.categoryproduct.model.dao.ProductMapper;
import com.jjzhong.mall.cloud.categoryproduct.model.upload.ProductData;

import java.util.List;

/**
 * 商品批量上传监听器
 */
public class ProductDataListener implements ReadListener<ProductData> {

    private static final int BATCH_COUNT = 5;
    private List<ProductData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    private final ProductMapper productMapper;

    public ProductDataListener(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public void invoke(ProductData data, AnalysisContext context) {
        // 达到 BATCH_COUNT 存储一次数据库，防止数据过多导致OOM
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成会调用此方法
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 将最后的数据保存
        saveData();
    }

    /**
     * 存储数据库
     */
    private void saveData() {
        productMapper.batchInsertUploadedProductData(cachedDataList);
    }
}
