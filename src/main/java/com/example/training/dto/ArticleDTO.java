package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * author id
     */
    private Integer userId;

    /**
     * article thumbnail
     */
    private String articleCover;

    /**
     * title
     */
    private String articleTitle;

    /**
     * content
     */
    private String articleContent;

    /**
     * likeCount
     */
    private Integer likeCount;

    /**
     * isLike
     */
    private Boolean isLike;

    /**
     * viewsCount
     */
    private Integer viewsCount;

    /**
     * commentsCount
     */
    private Integer commentsCount;

    /**
     * article type
     */
    private Integer type;

    /**
     * original link
     */
    private String originalUrl;

    /**
     * publication time
     */
    private LocalDateTime createTime;

    /**
     * updateTime
     */
    private LocalDateTime updateTime;

    /**
     * categoryId
     */
    private Integer categoryId;

    /**
     * categoryName
     */
    private String categoryName;

    /**
     * article tags
     */
    private List<TagDTO> tagDTOList;

    /**
     * lastArticle
     */
    private ArticlePaginationDTO lastArticle;

    /**
     * nextArticle
     */
    private ArticlePaginationDTO nextArticle;

    /**
     * recommendArticleList
     */
    private List<ArticleRecommendDTO> recommendArticleList;

    /**
     * newestArticleList
     */
    private List<ArticleRecommendDTO> newestArticleList;

    /**
     * nickname
     */
    private String nickname;

    /**
     * avatar
     */
    private String avatar;
}
