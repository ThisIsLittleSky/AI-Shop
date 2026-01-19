package com.sky.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.sky.config.OssConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * OSS工具类
 * 用于文件上传、删除等操作
 */
@Component
public class OssUtil {
    
    @Autowired
    private OssConfig ossConfig;
    
    /**
     * 上传文件到OSS
     * @param file 上传的文件
     * @param folder 存储文件夹（如：avatar、goods等）
     * @return 文件的访问URL
     */
    public String uploadFile(MultipartFile file, String folder) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        
        // 验证文件类型
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        // 验证图片格式
        String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"};
        boolean isValidExtension = false;
        for (String ext : allowedExtensions) {
            if (extension.equalsIgnoreCase(ext)) {
                isValidExtension = true;
                break;
            }
        }
        
        if (!isValidExtension) {
            throw new IllegalArgumentException("不支持的文件格式，仅支持：jpg, jpeg, png, gif, bmp, webp");
        }
        
        // 验证文件大小（5MB）
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("文件大小不能超过5MB");
        }
        
        // 生成唯一文件名
        String fileName = UUID.randomUUID().toString() + extension;
        String objectName = folder + "/" + fileName;
        
        // 如果配置了路径前缀，添加到objectName前面
        if (ossConfig.getPathPrefix() != null && !ossConfig.getPathPrefix().isEmpty()) {
            String prefix = ossConfig.getPathPrefix().endsWith("/") 
                    ? ossConfig.getPathPrefix() 
                    : ossConfig.getPathPrefix() + "/";
            objectName = prefix + objectName;
        }
        
        // 创建OSS客户端
        OSS ossClient = new OSSClientBuilder().build(
                ossConfig.getEndpoint(),
                ossConfig.getAccessKeyId(),
                ossConfig.getAccessKeySecret()
        );
        
        try {
            // 上传文件
            InputStream inputStream = file.getInputStream();
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    ossConfig.getBucketName(),
                    objectName,
                    inputStream
            );
            
            ossClient.putObject(putObjectRequest);
            
            // 构建文件访问URL
            String fileUrl;
            if (ossConfig.getDomain() != null && !ossConfig.getDomain().isEmpty()) {
                // 使用自定义域名
                fileUrl = ossConfig.getDomain() + "/" + objectName;
            } else {
                // 使用OSS默认域名
                fileUrl = "https://" + ossConfig.getBucketName() + "." + 
                         ossConfig.getEndpoint() + "/" + objectName;
            }
            
            return fileUrl;
        } finally {
            // 关闭OSS客户端
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
    
    /**
     * 上传头像文件（快捷方法）
     */
    public String uploadAvatar(MultipartFile file) throws Exception {
        return uploadFile(file, "avatar");
    }
    
    /**
     * 删除OSS中的文件
     * @param fileUrl 文件的完整URL
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }
        
        // 从URL中提取objectName
        String objectName = extractObjectNameFromUrl(fileUrl);
        if (objectName == null || objectName.isEmpty()) {
            return;
        }
        
        OSS ossClient = new OSSClientBuilder().build(
                ossConfig.getEndpoint(),
                ossConfig.getAccessKeyId(),
                ossConfig.getAccessKeySecret()
        );
        
        try {
            ossClient.deleteObject(ossConfig.getBucketName(), objectName);
        } catch (Exception e) {
            // 记录日志，但不抛出异常
            System.err.println("删除OSS文件失败：" + e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
    
    /**
     * 从URL中提取objectName
     */
    private String extractObjectNameFromUrl(String fileUrl) {
        try {
            // 处理自定义域名
            if (ossConfig.getDomain() != null && !ossConfig.getDomain().isEmpty() 
                    && fileUrl.contains(ossConfig.getDomain())) {
                return fileUrl.substring(fileUrl.indexOf(ossConfig.getDomain()) + 
                                       ossConfig.getDomain().length() + 1);
            }
            
            // 处理OSS默认域名
            String bucketDomain = ossConfig.getBucketName() + "." + ossConfig.getEndpoint();
            if (fileUrl.contains(bucketDomain)) {
                return fileUrl.substring(fileUrl.indexOf(bucketDomain) + bucketDomain.length() + 1);
            }
            
            // 如果都不匹配，尝试从URL路径中提取
            int lastSlashIndex = fileUrl.lastIndexOf("/");
            if (lastSlashIndex >= 0 && lastSlashIndex < fileUrl.length() - 1) {
                return fileUrl.substring(lastSlashIndex + 1);
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
