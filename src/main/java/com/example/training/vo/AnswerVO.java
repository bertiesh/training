package com.example.training.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Xuxinyuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "答案")
public class AnswerVO {
    /**
     * 答案id
     */
    @NotBlank(message = "答案id不能为空")
    @ApiModelProperty(name = "id", value = "答案id", dataType = "Integer")
    private Integer id;

    /**
     * 题目解析
     */
    @ApiModelProperty(name = "questionAnalysis", value = "题目解析", required = true, dataType = "String")
    private String questionAnalysis;

    /**
     * 题目类型
     */
    @ApiModelProperty(name = "type", value = "题目类型", dataType = "Integer")
    private Integer type;

    /**
     * 题目状态 1.问卷 2.练习
     */
    @ApiModelProperty(name = "status", value = "题目状态", dataType = "String")
    private Integer status;

    /**
     * 是否删除
     */
    @ApiModelProperty(name = "isDelete", value = "是否删除", dataType = "Boolean")
    private Boolean isDelete;

    /**
     * 图片
     */
    @ApiModelProperty(name = "images", value = "答案图片", dataType = "String")
    private String images;

    /**
     * 可选答案
     */
    @ApiModelProperty(name = "possibleAnswers", value = "可选答案", dataType = "String")
    private String possibleAnswers;

    /**
     * 正确答案
     */
    @ApiModelProperty(name = "correctAnswers", value = "正确答案", dataType = "String")
    private String correctAnswers;
}
