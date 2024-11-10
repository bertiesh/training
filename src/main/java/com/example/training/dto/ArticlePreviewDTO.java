package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * article preview
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePreviewDTO {
    /**
     * article id
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
     * createTime
     */
    private Date createTime;

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
