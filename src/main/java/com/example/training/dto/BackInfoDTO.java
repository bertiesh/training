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
@NoArgsConstructor
@AllArgsConstructor
public class BackInfoDTO {
    /**
     * visits
     */
    private Integer viewsCount;

    /**
     * volume of messages
     */
    private Integer messageCount;

    /**
     * number of users
     */
    private Integer userCount;

    /**
     * volume of articles
     */
    private Integer articleCount;

    /**
     * classification statistics
     */
    private List<CategoryDTO> categoryDTOList;

    /**
     * tag list
     */
    private List<TagDTO> tagDTOList;

    /**
     * article statistics list
     */
    private List<ArticleStatisticsDTO> articleStatisticsList;

    /**
     * collection of users per week
     */
    private List<UniqueViewDTO> uniqueViewDTOList;

    /**
     * article views ranking
     */
    private List<ArticleRankDTO> articleRankDTOList;
}
