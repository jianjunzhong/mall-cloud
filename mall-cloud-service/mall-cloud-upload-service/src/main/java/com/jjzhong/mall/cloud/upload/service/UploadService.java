package com.jjzhong.mall.cloud.upload.service;

import com.jjzhong.mall.cloud.sdk.upload.response.FileUploadRes;
import com.jjzhong.mall.cloud.sdk.upload.response.ImageUploadRes;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    ImageUploadRes uploadImage(MultipartFile file);
    FileUploadRes uploadFile(MultipartFile file);
    ImageUploadRes generateAndUploadQrCode(String orderNo);
}
