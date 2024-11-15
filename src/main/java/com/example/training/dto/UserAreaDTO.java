package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xinyuan Xu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAreaDTO {
    /**
     * 地区名
     */
    private String name;

    /**
     * 数量
     */
    private Long value;
}
