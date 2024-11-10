package com.example.training.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Xuxinyuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_answer")
public class Answer {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 作者
     */
    private Integer userId;

    /**
     * 题目解析
     */
    private String questionAnalysis;

    /**
     * 题目类型
     */
    private Integer type;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 题目状态 1.问卷 2.练习
     */
    private Integer status;

    /**
     * 图片
     */
    private String images;

    /**
     * 可选答案
     */
    private String possibleAnswers;

    /**
     * 正确答案
     */
    private String correctAnswers;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
