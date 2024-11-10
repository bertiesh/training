package com.example.training.strategy;

import com.example.training.dto.ArticleSearchDTO;

import java.util.List;

/**
 * @author Xuxinyuan
 */
public interface SearchStrategy {
    /**
     * 搜索文章
     *
     * @param keywords 关键字
     * @return {@link List <ArticleSearchDTO>} 文章列表
     */
    List<ArticleSearchDTO> searchArticle(String keywords);
}
