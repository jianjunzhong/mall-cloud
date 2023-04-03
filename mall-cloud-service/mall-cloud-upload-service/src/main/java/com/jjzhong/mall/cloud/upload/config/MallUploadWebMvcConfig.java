package com.jjzhong.mall.cloud.upload.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import com.jjzhong.mall.cloud.upload.constant.UploadConstant;

/**
 * 上传服务的WebMVC配置
 */
@Configuration
public class MallUploadWebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 配置资源路径，如将 http://localhost:9000/images/xxx.jpg 映射到本地目录 upload/images/xxx.jpg
     * @param registry 注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置图片的资源路径
        registry.addResourceHandler("/" + UploadConstant.UPLOAD_IMAGE_CONTEXT + "/**")
                .addResourceLocations("file:" + UploadConstant.UPLOAD_DIR + UploadConstant.UPLOAD_IMAGE_CONTEXT + "/");
    }
}
