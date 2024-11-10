package com.example.training.strategy;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Xuxinyuan
 */
public interface ArticleImportStrategy {
    /**
     * 导入文章
     *
     * @param file 文件
     */
    void importArticles(MultipartFile file);
}
