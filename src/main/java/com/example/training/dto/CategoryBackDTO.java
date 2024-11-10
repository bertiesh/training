package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Backstage comments
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryBackDTO {
    /**
     * category id
     */
    private Integer id;

    /**
     * categoryName
     */
    private String categoryName;

    /**
     * articleCount
     */
    private Integer articleCount;

    /**
     * createTime
     */
    private LocalDateTime createTime;
}
