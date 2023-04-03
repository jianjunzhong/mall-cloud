package com.jjzhong.mall.cloud.cartorder.feign;

import com.jjzhong.mall.cloud.cartorder.feign.fallback.UploadClientFallback;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import com.jjzhong.mall.cloud.sdk.upload.response.ImageUploadRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * OpenFeign 的上传服务调用客户端
 */
@FeignClient(
        value = "mall-cloud-upload-service",
        fallback = UploadClientFallback.class
)
@Primary
public interface UploadClient {
    @RequestMapping(value = "/mall-cloud-upload-service/generate/qrcode",
            method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    CommonResponse<ImageUploadRes> generateQrCode(@RequestParam("orderNo") String orderNo);
}
