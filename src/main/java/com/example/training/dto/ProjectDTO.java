package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    /**
     * 项目名
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 封面
     */
    private String cover;

    /**
     * 状态值 1 问卷 2 练习
     */
    private Integer status;

    /**
     * 题目
     */
    private String questions;

    /**
     * 题目列表
     */
    private List<QuestionDTO> questionDTOs;

    /**
     * 级联情况
     */
    private String cascades;

    /**
     * 级联情况表
     */
    private Map<Integer, Map<Integer, Integer>> cascadesMapList;

    /**
     * 是否显示题号
     */
    private Boolean questionNumber;

    /**
     * 是否需要密码
     */
    private Boolean isPassword;

    /**
     * 是否显示答题进度
     */
    private Boolean progressBar;

    /**
     * 是否自动保存选择
     */
    private Boolean autoSave;

    /**
     * 答题卡是否可见
     */
    private Boolean answerSheetVisible;

    /**
     * 是否允许拷贝问题
     */
    private Boolean copyEnabled;

    /**
     * 是否允许改答案
     */
    private Boolean enableUpdate;

    /**
     * 是否显示答案及解析
     */
    private Boolean answerAnalysis;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
