package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * archive articles
 * @author Xinyuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArchiveDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * author id
     */
    private Integer userId;

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
     * commentsCount
     */
    private Integer commentsCount;

    /**
     * isLike
     */
    private Boolean isLike;

    /**
     * publication time
     */
    private LocalDateTime createTime;
}
