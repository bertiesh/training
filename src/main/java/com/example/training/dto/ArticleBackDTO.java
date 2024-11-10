package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * backend article
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleBackDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * user id
     */
    private Integer userId;

    /**
     * articleCover
     */
    private String articleCover;

    /**
     * title
     */
    private String articleTitle;

    /**
     * publication time
     */
    private LocalDateTime createTime;

    /**
     * likeCount
     */
    private Integer likeCount;

    /**
     * viewsCount
     */
    private Integer viewsCount;

    /**
     * categoryName
     */
    private String categoryName;

    /**
     * article tags
     */
    private List<TagDTO> tagDTOList;

    /**
     * article type
     */
    private Integer type;

    /**
     * isTop
     */
    private Integer isTop;

    /**
     * isDelete
     */
    private Integer isDelete;

    /**
     * isReview
     */
    private Integer isReview;

    /**
     * article status
     */
    private Integer status;
}
