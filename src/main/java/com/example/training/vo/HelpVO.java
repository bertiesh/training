package com.example.training.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Xuxinyuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "帮助")
public class HelpVO {
    /**
     * 题目id
     */
    @ApiModelProperty(name = "id", value = "题目id", dataType = "Integer")
    private Integer id;

    /**
     * 题目
     */
    @ApiModelProperty(name = "question", value = "题目", required = true, dataType = "String")
    private String question;

    /**
     * 回答
     */
    @ApiModelProperty(name = "answer", value = "回答", required = true, dataType = "String")
    private String answer;

    /**
     * 分类
     */
    @ApiModelProperty(name = "category", value = "题目分类", dataType = "String")
    private String category;

    /**
     * 目的
     */
    @ApiModelProperty(name = "intention", value = "题目目的", dataType = "String")
    private String intention;

    /**
     * 关联问题
     */
    @ApiModelProperty(name = "questions", value = "关联问题", dataType = "String")
    private String synonymicQuestions;

//    /**
//     * 题目关键词
//     */
//    @ApiModelProperty(name = "standardQuestionFeatures", value = "题目关键词", dataType = "List<String>")
//    private List<String> standardQuestionFeatures;
//
//    /**
//     * 相似题目关键词
//     */
//    @ApiModelProperty(name = "synonymicQuestionFeatures", value = "相似题目关键词", dataType = "List<String>")
//    private List<String> synonymicQuestionFeatures;

    /**
     * 题目状态 1.常见 0.不常见
     */
    @ApiModelProperty(name = "status", value = "题目状态", dataType = "Integer")
    private Boolean status;

    /**
     * 是否删除
     */
    @ApiModelProperty(name = "isDelete", value = "是否删除", dataType = "Boolean")
    private Boolean isDelete;
}
