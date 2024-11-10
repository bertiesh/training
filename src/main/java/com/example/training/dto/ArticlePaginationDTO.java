package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Previous article and next article
 * @author Xinyuan Xu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticlePaginationDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * articleCover
     */
    private String articleCover;

    /**
     * title
     */
    private String articleTitle;
}
