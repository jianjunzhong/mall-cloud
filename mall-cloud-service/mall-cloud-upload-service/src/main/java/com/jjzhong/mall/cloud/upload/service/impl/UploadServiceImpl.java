package com.jjzhong.mall.cloud.upload.service.impl;

import com.google.zxing.WriterException;
import com.jjzhong.mall.cloud.sdk.upload.response.FileUploadRes;
import com.jjzhong.mall.cloud.sdk.upload.response.ImageUploadRes;
import com.jjzhong.mall.cloud.upload.constant.UploadConstant;
import com.jjzhong.mall.cloud.upload.exception.MallUploadServiceException;
import com.jjzhong.mall.cloud.upload.exception.MallUploadServiceExceptionEnum;
import com.jjzhong.mall.cloud.upload.service.UploadService;
import com.jjzhong.mall.cloud.upload.util.FileUtils;
import com.jjzhong.mall.cloud.upload.util.QRCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 上传服务
 */
@Slf4j
@Service
public class UploadServiceImpl implements UploadService {
    /**
     * 上传图片
     * @param file 接收到的文件
     * @return 上传结果
     */
    @Override
    public ImageUploadRes uploadImage(MultipartFile file) {
        ImageUploadRes res;
        try {
            File newFile = upload(file, UploadConstant.UPLOAD_IMAGE_CONTEXT);
            // 缩小图片
            Thumbnails.of(newFile)
                    .size(UploadConstant.IMAGE_SIZE, UploadConstant.IMAGE_SIZE)
//                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(Constant.FILE_UPLOAD_DIR + Constant.WATER_MARK_JPG)), Constant.IMAGE_OPACITY)
                    .toFile(newFile);
            res = new ImageUploadRes(getURI(UploadConstant.UPLOAD_IMAGE_CONTEXT) + "/" + newFile.getName());
        } catch (Exception e) {
            throw new MallUploadServiceException(MallUploadServiceExceptionEnum.UPLOAD_FAILED);
        }
        return res;
    }

    /**
     * 上传文件
     * @param file 前端传输的文件
     * @return 上传的结果
     */
    @Override
    public FileUploadRes uploadFile(MultipartFile file) {
        FileUploadRes res;
        try {
            File newFile = upload(file, UploadConstant.UPLOAD_FILE_CONTEXT);
            res = new FileUploadRes(getURI(UploadConstant.UPLOAD_FILE_CONTEXT) + "/" + newFile.getName());
            return res;
        } catch (Exception e) {
            throw new MallUploadServiceException(MallUploadServiceExceptionEnum.UPLOAD_FAILED);
        }
    }

    /**
     * 生成二维码，保存到本地并生成访问链接
     * @param orderNo 订单号
     * @return 二维码 url
     */
    @Override
    public ImageUploadRes generateAndUploadQrCode(String orderNo) {
        ImageUploadRes res;
        try {
            String payUrl = "http://" + UploadConstant.ORDER_PAY_HOST + "/pay?orderNo=" + orderNo;
            String dstDir = UploadConstant.UPLOAD_DIR + UploadConstant.UPLOAD_IMAGE_CONTEXT + "/qrcode/";
            FileUtils.makeDirsIfNotExists(dstDir);
            QRCodeGenerator.generateQRCodeImage(payUrl,
                    350,
                    350,
                    dstDir + orderNo + ".png");
            URI uri = getURI(UploadConstant.UPLOAD_IMAGE_CONTEXT);
            String pngAddr = uri + "/qrcode/" + orderNo + ".png";
            res = new ImageUploadRes(pngAddr);
            return res;
        } catch (WriterException | IOException | MallUploadServiceException e) {
            throw new MallUploadServiceException(MallUploadServiceExceptionEnum.UPLOAD_FAILED);
        }
    }

    /**
     * 获取配置中的协议（http/https）、主机、端口号，并将其拼接成可供用户访问的 URI
     * @param context 路径上下文
     * @return 用户可以访问的有效的 URI
     */
    private URI getURI(String context) {
        URI effectiveURI;
        try {
            effectiveURI = new URI(UploadConstant.UPLOAD_SCHEME, null, UploadConstant.UPLOAD_HOST, UploadConstant.UPLOAD_PORT, "/" + context, null, null);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return effectiveURI;
    }

    /**
     * 此方法会生成 UUID 文件名，并将文件保存到指定的目录（若目录不存在，则自动生成
     * @param file 用户通过http请求上传的文件
     * @param context 路径上下文
     * @return 保存在目录中的新文件
     * @throws IOException 保存在本地目录失败抛出的异常
     */
    private File upload(MultipartFile file, String context) throws IOException {
        String newFilename = FileUtils.generateNewFileName(file);
        String dstDir = UploadConstant.UPLOAD_DIR + context + "/";
        FileUtils.makeDirsIfNotExists(dstDir);
        String newFilePath = dstDir + newFilename;
        File newFile = new File(newFilePath);
        file.transferTo(newFile);
        return newFile;
    }

}
