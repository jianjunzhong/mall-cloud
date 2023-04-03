package com.jjzhong.mall.cloud.categoryproduct.model.vo;

import com.jjzhong.mall.cloud.categoryproduct.constant.AsyncTaskStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 异步任务的执行信息类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsyncTaskInfoVO {
    private String taskId;
    private AsyncTaskStatusEnum status;
    private Date startTime;
    private Date endTIme;
    private String totalTime;
}
