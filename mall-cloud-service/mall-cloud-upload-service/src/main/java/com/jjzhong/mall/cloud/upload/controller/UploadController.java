package com.jjzhong.mall.cloud.upload.controller;

import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import com.jjzhong.mall.cloud.sdk.upload.response.FileUploadRes;
import com.jjzhong.mall.cloud.sdk.upload.response.ImageUploadRes;
import com.jjzhong.mall.cloud.upload.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "上传接口")
@RestController
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload/image")
    @Operation(description = "上传图片")
    public CommonResponse<ImageUploadRes> uploadImage(@RequestParam("file") MultipartFile file) {
        return CommonResponse.success(uploadService.uploadImage(file));
    }

    @PostMapping("/upload/file")
    @Operation(description = "上传文件")
    public CommonResponse<FileUploadRes> uploadProduct(@RequestParam("file") MultipartFile file) {
        return CommonResponse.success(uploadService.uploadFile(file));
    }

    @PostMapping("/generate/qrcode")
    @Operation(description = "生成二维码")
    public CommonResponse<ImageUploadRes> generateQrCode(@RequestParam("orderNo") String orderNo) {
        return CommonResponse.success(uploadService.generateAndUploadQrCode(orderNo));
    }
}
