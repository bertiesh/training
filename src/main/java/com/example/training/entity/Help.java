package com.example.training.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xuxinyuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_help")
public class Help {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 客服问题
     */
    private String question;

    /**
     * 客服回答
     */
    private String answer;

    /**
     * 问题分类
     */
    private String category;

    /**
     * 提问目的
     */
    private String intention;

    /**
     * 问题关键词
     */
    private String standardQuestionFeatures;

    /**
     * 相似问题
     */
    private String synonymicQuestions;

    /**
     * 相似问题关键词
     */
    private String synonymicQuestionsFeatures;

    /**
     * 是否常见
     */
    private Boolean status;

    /**
     * 是否删除
     */
    private Boolean isDelete;
}
