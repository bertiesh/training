package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * recommend article
 * @author Xinyuan Xu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleRecommendDTO {
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
     * createTime
     */
    private LocalDateTime createTime;
}
