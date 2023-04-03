package com.jjzhong.mall.cloud.categoryproduct.feign.fallback;

import com.jjzhong.mall.cloud.categoryproduct.exception.MallCategoryProductServiceException;
import com.jjzhong.mall.cloud.categoryproduct.exception.MallCategoryProductServiceExceptionEnum;
import com.jjzhong.mall.cloud.categoryproduct.feign.UploadClient;
import com.jjzhong.mall.cloud.common.exception.MallCommonException;
import com.jjzhong.mall.cloud.common.exception.MallCommonExceptionEnum;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import com.jjzhong.mall.cloud.sdk.upload.response.FileUploadRes;
import com.jjzhong.mall.cloud.sdk.upload.response.ImageUploadRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传服务调用客户端的熔断降级处理
 */
@Slf4j
@Component
public class UploadClientFallback implements UploadClient {
    @Override
    public CommonResponse<ImageUploadRes> uploadImage(MultipartFile file) {
        throw new MallCommonException(MallCommonExceptionEnum.UPLOAD_SERVICE_ERROR);
    }

    @Override
    public CommonResponse<FileUploadRes> uploadFile(MultipartFile file) {
        throw new MallCommonException(MallCommonExceptionEnum.UPLOAD_SERVICE_ERROR);
    }
}
