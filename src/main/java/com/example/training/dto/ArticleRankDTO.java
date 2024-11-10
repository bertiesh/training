package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRankDTO {
    /**
     * article id
     */
    private Integer id;

    /**
     * title
     */
    private String articleTitle;

    /**
     * content
     */
    private String articleContent;

    /**
     * viewsCount
     */
    private Integer viewsCount;
}
