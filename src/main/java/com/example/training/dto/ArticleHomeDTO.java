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
public class ArticleHomeDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * articleCover
     */
    private String articleCover;

    /**
     * articleTitle
     */
    private String articleTitle;

    /**
     * articleContent
     */
    private String articleContent;

    /**
     * createTime
     */
    private LocalDateTime createTime;

    /**
     * isTop
     */
    private Integer isTop;

    /**
     * type
     */
    private Integer type;

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
}
