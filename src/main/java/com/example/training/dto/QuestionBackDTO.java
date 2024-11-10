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
public class QuestionBackDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 创建人
     */
    private Integer userId;

    /**
     * 题目
     */
    private String questionTitle;

    /**
     * 题目描述
     */
    private String questionDescription;

    /**
     * 发表时间
     */
    private LocalDateTime createTime;

    /**
     * 题目标签
     */
    private List<TagDTO> tagDTOList;

    /**
     * 题目类型
     */
    private Integer type;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 题目状态
     */
    private Integer status;

    /**
     * 图片
     */
    private String images;

    /**
     * 题目
     */
    private String questions;

    /**
     * 图片列表
     */
    private List<String> imgList;

    /**
     * 题目列表
     */
    private List<Integer> questionList;

    /**
     * 答案
     */
    private AnswerBackDTO answerBackDTO;
}
