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
@ApiModel(description = "题目")
public class QuestionVO {
    /**
     * 题目id
     */
    @ApiModelProperty(name = "id", value = "题目id", dataType = "Integer")
    private Integer id;

    /**
     * 题目标题
     */
    @NotBlank(message = "题目标题不能为空")
    @ApiModelProperty(name = "questionTitle", value = "题目标题", required = true, dataType = "String")
    private String questionTitle;

    /**
     * 介绍
     */
    @ApiModelProperty(name = "questionDescription", value = "题目介绍", required = true, dataType = "String")
    private String questionDescription;

    /**
     * 题目标签
     */
    @ApiModelProperty(name = "tagNameList", value = "题目标签", dataType = "List<String>")
    private List<String> tagNameList;

    /**
     * 题目类型
     */
    @ApiModelProperty(name = "type", value = "题目类型", dataType = "Integer")
    private Integer type;

    /**
     * 题目状态 1.问卷 2.练习
     */
    @ApiModelProperty(name = "status", value = "题目状态", dataType = "Integer")
    private Integer status;

    /**
     * 是否删除
     */
    @ApiModelProperty(name = "isDelete", value = "是否删除", dataType = "Integer")
    private Integer isDelete;

    /**
     * 图片
     */
    @ApiModelProperty(name = "images", value = "题目图片", dataType = "String")
    private String images;

    /**
     * 问题
     */
    @ApiModelProperty(name = "questions", value = "关联问题", dataType = "String")
    private String questions;
}
