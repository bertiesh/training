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
public class QuestionBackAnalysisDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 题目
     */
    private String questionTitle;

    /**
     * 题目描述
     */
    private String questionDescription;

    /**
     * 题目类型
     */
    private Integer type;

    /**
     * 题目状态
     */
    private Integer status;

    /**
     * 图片
     */
    private String images;

    /**
     * 图片列表
     */
    private List<String> imgList;

    /**
     * 分析
     */
    private AnalysisDTO analysisDTO;

    /**
     * 作答人数
     */
    private Integer submitNums;
}