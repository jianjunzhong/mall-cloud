package com.jjzhong.mall.cloud.upload.util;

import com.jjzhong.mall.cloud.upload.exception.MallUploadServiceException;
import com.jjzhong.mall.cloud.upload.exception.MallUploadServiceExceptionEnum;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * 文件工具类
 */
public class FileUtils {
    /**
     * 生成 UUID 新文件名
     * @param file 接收到的文件
     * @return 新文件名（含文件后缀）
     */
    public static String generateNewFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String suffix = StringUtils.getFilenameExtension(originalFilename);
        UUID uuid = UUID.randomUUID();
        return uuid + "." + suffix;
    }

    /**
     * 如果文件夹不存在则创建
     * @param directory 目标文件夹
     */
    public static void makeDirsIfNotExists(String directory) {
        File dstDir = new File(directory);
        if (!dstDir.exists()) {
            try {
                boolean mkdirs = dstDir.mkdirs();
                if (!mkdirs)
                    throw new MallUploadServiceException(MallUploadServiceExceptionEnum.DIR_CREATE_FAILED);
            } catch (SecurityException e) {
                throw new MallUploadServiceException(MallUploadServiceExceptionEnum.DIR_CREATE_FAILED);
            }
        }
    }
}
