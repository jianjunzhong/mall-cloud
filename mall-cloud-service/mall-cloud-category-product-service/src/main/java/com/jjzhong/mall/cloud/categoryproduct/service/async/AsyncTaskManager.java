package com.jjzhong.mall.cloud.categoryproduct.service.async;

import com.jjzhong.mall.cloud.categoryproduct.constant.AsyncTaskStatusEnum;
import com.jjzhong.mall.cloud.categoryproduct.exception.MallCategoryProductServiceException;
import com.jjzhong.mall.cloud.categoryproduct.exception.MallCategoryProductServiceExceptionEnum;
import com.jjzhong.mall.cloud.categoryproduct.model.vo.AsyncTaskInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 异步任务管理器，用于对异步任务进行包装，管理异步任务信息
 */
@Slf4j
@Component
public class AsyncTaskManager {
    private final Map<String, AsyncTaskInfoVO> taskInfoContainer = new HashMap<>(16);
    @Autowired
    private AsyncService asyncService;

    /**
     * 设置异步任务 id 并放入 HashMap 中
     * @return 异步任务信息
     */
    public AsyncTaskInfoVO initTask() {
        // 设置唯一的异步任务 id
        AsyncTaskInfoVO asyncTaskInfoVO = new AsyncTaskInfoVO();
        asyncTaskInfoVO.setTaskId(UUID.randomUUID().toString());
        asyncTaskInfoVO.setStartTime(new Date());
        asyncTaskInfoVO.setStatus(AsyncTaskStatusEnum.STARTED);
        // 将异步任务放入存储容器中
        setTaskInfo(asyncTaskInfoVO);
        return asyncTaskInfoVO;
    }

    /**
     * 初始化任务信息，并执行任务
     * @param file 传入的 Excel 文件
     * @return 异步任务信息
     */
    public AsyncTaskInfoVO submit(MultipartFile file) {
        AsyncTaskInfoVO asyncTaskInfoVO = initTask();
        try {
            asyncService.asyncExcelImport(file.getInputStream(), asyncTaskInfoVO.getTaskId());
        } catch (IOException e) {
            throw new MallCategoryProductServiceException(MallCategoryProductServiceExceptionEnum.PRODUCT_IMPORT_ERROR);
        }
        return asyncTaskInfoVO;
    }

    public void setTaskInfo(AsyncTaskInfoVO asyncTaskInfoVO) {
        taskInfoContainer.put(asyncTaskInfoVO.getTaskId(), asyncTaskInfoVO);
    }

    public AsyncTaskInfoVO getTaskInfo(String taskId) {
        return taskInfoContainer.get(taskId);
    }
}
