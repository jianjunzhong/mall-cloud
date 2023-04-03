package com.jjzhong.mall.cloud.categoryproduct.service.async;

import org.springframework.scheduling.annotation.Async;

import java.io.InputStream;

/**
 * 异步服务接口
 */
public interface AsyncService {
    @Async("getAsyncExecutor")
    void asyncExcelImport(InputStream inputStream, String taskId);
}
