package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionPostDTO {
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
     * 答案
     */
    private AnswerDTO answerDTO;
}