package com.jjzhong.mall.cloud.categoryproduct.feign;

import com.jjzhong.mall.cloud.categoryproduct.feign.fallback.UploadClientFallback;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import com.jjzhong.mall.cloud.sdk.upload.response.FileUploadRes;
import com.jjzhong.mall.cloud.sdk.upload.response.ImageUploadRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

/**
 * OpenFeign 的上传服务调用客户端
 */
@FeignClient(
        value = "mall-cloud-upload-service",
        fallback = UploadClientFallback.class
)
@Primary
public interface UploadClient {
    @RequestMapping(value = "/mall-cloud-upload-service/upload/image",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<ImageUploadRes> uploadImage(MultipartFile file);

    @RequestMapping(value = "/mall-cloud-upload-service/upload/file",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<FileUploadRes> uploadFile(MultipartFile file);
}
