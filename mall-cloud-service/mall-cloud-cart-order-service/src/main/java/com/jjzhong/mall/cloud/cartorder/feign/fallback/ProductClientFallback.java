package com.jjzhong.mall.cloud.cartorder.feign.fallback;

import com.jjzhong.mall.cloud.cartorder.exception.MallCartOrderServiceException;
import com.jjzhong.mall.cloud.cartorder.exception.MallCartOrderServiceExceptionEnum;
import com.jjzhong.mall.cloud.cartorder.feign.ProductClient;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import com.jjzhong.mall.cloud.sdk.product.request.RestoreStockReq;
import com.jjzhong.mall.cloud.sdk.product.request.DeductStockReq;
import com.jjzhong.mall.cloud.sdk.product.vo.ProductVO;
import com.jjzhong.mall.cloud.sdk.product.request.UpdateStockReq;
import org.springframework.stereotype.Component;

/**
 * ProductClient 的熔断降级处理类，该类中抛出的异常可以被全局异常处理类捕获
 */
@Component
public class ProductClientFallback implements ProductClient {
    @Override
    public CommonResponse<ProductVO> detail(Integer id) {
        throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.CATEGORY_PRODUCT_SERVICE_ERROR);
    }

    @Override
    public CommonResponse<Object> updateStock(UpdateStockReq updateStockReq) {
        throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.CATEGORY_PRODUCT_SERVICE_ERROR);
    }

    @Override
    public CommonResponse<Object> deductStock(DeductStockReq deductStockReq) {
        throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.CATEGORY_PRODUCT_SERVICE_ERROR);
    }

    @Override
    public CommonResponse<Object> restore(RestoreStockReq restoreStockReq) {
        throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.CATEGORY_PRODUCT_SERVICE_ERROR);
    }
}
