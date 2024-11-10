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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "模板")
public class TemplateVO {
    /**
     * 模板id
     */
    @ApiModelProperty(name = "id", value = "模板id", required = true, dataType = "Integer")
    private Integer id;

    /**
     * 模板名
     */
    @NotBlank(message = "模板名不能为空")
    @ApiModelProperty(name = "templateName", value = "模板名", required = true, dataType = "String")
    private String templateName;

    /**
     * 模板描述
     */
    @ApiModelProperty(name = "templateDesc", value = "模板描述", dataType = "String")
    private String templateDesc;

    /**
     * 模板封面
     */
    @NotBlank(message = "模板封面不能为空")
    @ApiModelProperty(name = "templateCover", value = "模板封面", required = true, dataType = "String")
    private String templateCover;

    /**
     * 状态值 1 问卷 2 练习
     */
    @ApiModelProperty(name = "status", value = "状态值", required = true, dataType = "Integer")
    private Integer status;

    /**
     * 题目
     */
    @ApiModelProperty(name = "questions", value = "题目", required = true, dataType = "String")
    private String questions;

    /**
     * 级联情况
     */
    @ApiModelProperty(name = "cascades", value = "级联情况", dataType = "String")
    private String cascades;

    /**
     * 是否删除
     */
    @ApiModelProperty(name = "isDelete", value = "是否删除", required = true, dataType = "Boolean")
    private Integer isDelete;
}
