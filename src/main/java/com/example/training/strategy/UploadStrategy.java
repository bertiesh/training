package com.example.training.strategy;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @author Xuxinyuan
 */
public interface UploadStrategy {
    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 上传路径
     * @return {@link String} 文件地址
     */
    String uploadFile(MultipartFile file, String path);

    /**
     * 上传文件
     *
     * @param fileName    文件名
     * @param inputStream 输入流
     * @param path        路径
     * @return {@link String}
     */
    String uploadFile(String fileName, InputStream inputStream, String path);

    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 上传路径
     * @return {@link String} 文件地址
     */
    List<String> uploadFiles(MultipartFile[] file, String path);
}
