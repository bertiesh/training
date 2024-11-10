package com.example.training.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemplateBackDTO {
    /**
     * 模板id
     */
    private Integer id;

    /**
     * 模板名
     */
    private String templateName;

    /**
     * 模板描述
     */
    private String templateDesc;

    /**
     * 模板封面
     */
    private String templateCover;

    /**
     * 是否删除
     */
    private Integer isDelete;

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
    private List<QuestionBackDTO> questionBackDTOS;

    /**
     * 级联情况
     */
    private String cascades;

    /**
     * 级联情况表
     */
    private Map<Integer, Map<Integer, Integer>> cascadesMapList;
}
