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
public class TagDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 标签名
     */
    private String tagName;
}
