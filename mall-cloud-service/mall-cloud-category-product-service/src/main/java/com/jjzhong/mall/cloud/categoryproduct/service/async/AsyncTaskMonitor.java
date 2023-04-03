package com.jjzhong.mall.cloud.categoryproduct.service.async;

import com.jjzhong.mall.cloud.categoryproduct.constant.AsyncTaskStatusEnum;
import com.jjzhong.mall.cloud.categoryproduct.model.vo.AsyncTaskInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 异步任务执行切面
 */
@Slf4j
@Aspect
@Component
public class AsyncTaskMonitor {
    @Autowired
    private AsyncTaskManager asyncTaskManager;

    /**
     * 对异步任务的执行过程进行监控
     * @param proceedingJoinPoint 加入点
     * @return 异步任务的执行结果
     */
    @Around("execution(* com.jjzhong.mall.cloud.categoryproduct.service.async.AsyncServiceImpl.*(..))")
    public Object handleTask(ProceedingJoinPoint proceedingJoinPoint) {
        // 获取到 taskId，从而对任务信息进行修改
        String taskId = proceedingJoinPoint.getArgs()[1].toString();
        AsyncTaskInfoVO taskInfo = asyncTaskManager.getTaskInfo(taskId);
        log.info("AsyncTaskMonitor is monitoring async task, id: {}", taskId);

        AsyncTaskStatusEnum status;
        Object result;
        try {
            // 执行异步任务
            taskInfo.setStatus(AsyncTaskStatusEnum.RUNNING);
            asyncTaskManager.setTaskInfo(taskInfo);
            result = proceedingJoinPoint.proceed();
            status = AsyncTaskStatusEnum.SUCCESS;
            log.info("AsyncTaskMonitor: async task ({}) execute successfully", taskId);
        } catch (Throwable e) {
            // 异步任务出现了异常
            status = AsyncTaskStatusEnum.FAILED;
            result = null;
            log.error("AsyncTaskMonitor: async task ({}) execute with error, info: {}",
                    taskId, e.getMessage(), e);
        }
        // 设置异步任务的其他信息，并再次放到容器中
        taskInfo.setStatus(status);
        taskInfo.setEndTIme(new Date());
        taskInfo.setTotalTime(String.valueOf(
                taskInfo.getEndTIme().getTime() - taskInfo.getStartTime().getTime()
        ));
        asyncTaskManager.setTaskInfo(taskInfo);
        return result;
    }
}
