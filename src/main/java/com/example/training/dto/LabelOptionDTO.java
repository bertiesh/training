package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 标签选项
 * @author Xuxinyuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LabelOptionDTO {
    /**
     * option id
     */
    private Integer id;

    /**
     * label name
     */
    private String label;

    /**
     * sub options
     */
    private List<LabelOptionDTO> children;
}
