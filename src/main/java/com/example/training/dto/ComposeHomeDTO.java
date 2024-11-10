package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComposeHomeDTO {
    /**
     * total followers
     */
    private Integer followers;

    /**
     * total number of articles
     */
    private Integer articleCount;

    /**
     * number of article displayed
     */
    private Integer articlesVisibleCount;

    /**
     * number of article reads
     */
    private Integer viewsCount;

    /**
     * article likes
     */
    private Integer likesCount;

    /**
     * article comments
     */
    private Integer commentsCount;
}
