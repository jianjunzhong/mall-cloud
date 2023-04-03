package com.jjzhong.mall.cloud.cartorder.feign.fallback;

import com.jjzhong.mall.cloud.cartorder.feign.UploadClient;
import com.jjzhong.mall.cloud.common.exception.MallCommonException;
import com.jjzhong.mall.cloud.common.exception.MallCommonExceptionEnum;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import com.jjzhong.mall.cloud.sdk.upload.response.ImageUploadRes;
import org.springframework.stereotype.Component;

/**
 * UploadClient 的熔断降级处理类，该类中抛出的异常可以被全局异常处理类捕获
 */
@Component
public class UploadClientFallback implements UploadClient {
    @Override
    public CommonResponse<ImageUploadRes> generateQrCode(String orderNo) {
        throw new MallCommonException(MallCommonExceptionEnum.UPLOAD_SERVICE_ERROR);
    }
}
