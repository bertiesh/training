package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * article preview list
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePreviewListDTO {
    /**
     * article list
     */
    private List<ArticlePreviewDTO> articlePreviewDTOList;

    /**
     * condition name
     */
    private String name;
}
