package com.jjzhong.mall.cloud.categoryproduct.controller;

import com.jjzhong.mall.cloud.categoryproduct.model.vo.AsyncTaskInfoVO;
import com.jjzhong.mall.cloud.categoryproduct.service.async.AsyncTaskManager;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "商品异步任务接口")
@RestController
@RequestMapping("/async/product")
public class AsyncProductController {
    @Autowired
    private AsyncTaskManager asyncTaskManager;

    @Operation(description = "异步导入商品信息")
    @PostMapping("/import")
    public CommonResponse<AsyncTaskInfoVO> importProduct(@RequestParam("file") MultipartFile file) {
        AsyncTaskInfoVO asyncTaskInfoVO = asyncTaskManager.submit(file);
        return CommonResponse.success(asyncTaskInfoVO);
    }

    @Operation(description = "查询异步任务信息")
    @GetMapping("/info")
    public CommonResponse<AsyncTaskInfoVO> getTaskInfo(@RequestParam("taskId") String taskId) {
        AsyncTaskInfoVO taskInfo = asyncTaskManager.getTaskInfo(taskId);
        return CommonResponse.success(taskInfo);
    }
}
