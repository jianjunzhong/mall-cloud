package com.jjzhong.mall.cloud.upload.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 上传服务常量
 */
@Component
public class UploadConstant {
    /** 图片压缩后的大小 */
    public static final Integer IMAGE_SIZE = 80;

    /** 返回的 uri 协议（http / https） */
    public static String UPLOAD_SCHEME;
    @Value("${mall.upload.schema}")
    public void setUploadScheme(String uploadScheme) {
        UPLOAD_SCHEME = uploadScheme;
    }

    /** 返回的 uri 主机地址 */
    public static String UPLOAD_HOST;
    @Value("${mall.upload.host}")
    public void setUploadHost(String uploadHost) {
        UPLOAD_HOST = uploadHost;
    }

    /** 返回的 uri 端口号 */
    public static Integer UPLOAD_PORT;
    @Value("${mall.upload.port}")
    public void setUploadPort(Integer uploadPort) {
        UPLOAD_PORT = uploadPort;
    }

    /** 上传的目标文件夹路径 */
    public static String UPLOAD_DIR;
    @Value("${mall.upload.dir}")
    public void setFileUploadDir(String uploadDir) {
        UPLOAD_DIR = uploadDir;
    }

    /** 支付服务的 socket */
    public static String ORDER_PAY_HOST;

    @Value("${mall.order.pay.host}")
    public void setOrderPayHost(String orderPayHost) {
        ORDER_PAY_HOST = orderPayHost;
    }

    /** 图片上传的上下文 */
    public static String UPLOAD_IMAGE_CONTEXT;

    @Value("${mall.upload.image.context}")
    public void setFileUploadImageContext(String uploadImageContext) {
        UPLOAD_IMAGE_CONTEXT = uploadImageContext;
    }

    /** 文件上传的上下文 */
    public static String UPLOAD_FILE_CONTEXT;
    @Value("${mall.upload.file.context}")
    public void setFileUploadFileContext(String uploadFileContext) {
        UPLOAD_FILE_CONTEXT = uploadFileContext;
    }
}
