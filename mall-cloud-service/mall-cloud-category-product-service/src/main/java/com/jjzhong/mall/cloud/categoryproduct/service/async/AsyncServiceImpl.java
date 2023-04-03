package com.jjzhong.mall.cloud.categoryproduct.service.async;

import com.alibaba.excel.EasyExcel;
import com.jjzhong.mall.cloud.categoryproduct.listener.ProductDataListener;
import com.jjzhong.mall.cloud.categoryproduct.model.dao.ProductMapper;
import com.jjzhong.mall.cloud.categoryproduct.model.upload.ProductData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

/**
 * 异步服务实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AsyncServiceImpl implements AsyncService {
    @Autowired
    private ProductMapper productMapper;

    @Async("getAsyncExecutor")
    @Override
    public void asyncExcelImport(InputStream inputStream, String taskId) {
        log.info("async task is running, id: {}", taskId);
        EasyExcel.read(inputStream, ProductData.class, new ProductDataListener(productMapper)).sheet().doRead();
    }
}
