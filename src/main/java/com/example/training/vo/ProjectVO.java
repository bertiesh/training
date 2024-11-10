package com.example.training.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Xuxinyuan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "项目")
public class ProjectVO {
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "id", dataType = "Integer")
    private Integer id;

    /**
     * 项目名
     */
    @ApiModelProperty(name = "name", value = "项目名", required = true, dataType = "String")
    private String name;

    /**
     * 问题id
     */
    @ApiModelProperty(name = "questions", value = "问题id列表", required = true, dataType = "String")
    private String questions;

    /**
     * 级联情况
     */
    @ApiModelProperty(name = "cascades", value = "级联情况", dataType = "String")
    private String cascades;

    /**
     * 1 问卷 2 练习
     */
    @ApiModelProperty(name = "status", value = "项目模式", required = true, dataType = "Integer")
    private Integer status;

    /**
     * 描述
     */
    @ApiModelProperty(name = "description", value = "description", required = true, dataType = "String")
    private String description;

    /**
     * 封面
     */
    @ApiModelProperty(name = "cover", value = "cover", required = true, dataType = "String")
    private String cover;

    /**
     * 是否删除
     */
    @ApiModelProperty(name = "isDelete", value = "isDelete", required = true, dataType = "Boolean")
    private Boolean isDelete;

    /**
     * 是否开放
     */
    @ApiModelProperty(name = "isActive", value = "是否开放", required = true, dataType = "Boolean")
    private Boolean isActive;

    /**
     * 是否随机生成
     */
    @ApiModelProperty(name = "isRandom", value = "是否随机生成", required = true, dataType = "Boolean")
    private Boolean isRandom;

    /**
     * 是否显示题号
     */
    @ApiModelProperty(name = "questionNumber", value = "是否显示题号", required = true, dataType = "Boolean")
    private Boolean questionNumber;

    /**
     * 密码
     */
    @ApiModelProperty(name = "password", value = "密码", required = true, dataType = "String")
    private String password;

    /**
     * 是否需要密码
     */
    @ApiModelProperty(name = "isPassword", value = "是否需要密码", required = true, dataType = "Boolean")
    private Boolean isPassword;

    /**
     * 是否显示答题进度
     */
    @ApiModelProperty(name = "progressBar", value = "是否显示答题进度", required = true, dataType = "Boolean")
    private Boolean progressBar;

    /**
     * 是否自动保存选择
     */
    @ApiModelProperty(name = "autoSave", value = "是否自动保存选择", required = true, dataType = "Boolean")
    private Boolean autoSave;

    /**
     * 答题卡是否可见
     */
    @ApiModelProperty(name = "answerSheetVisible", value = "答题卡是否可见", required = true, dataType = "Boolean")
    private Boolean answerSheetVisible;

    /**
     * 是否允许拷贝问题
     */
    @ApiModelProperty(name = "copyEnabled", value = "是否允许拷贝问题", required = true, dataType = "Boolean")
    private Boolean copyEnabled;

    /**
     * 是否允许改答案
     */
    @ApiModelProperty(name = "enableUpdate", value = "是否允许改答案", required = true, dataType = "Boolean")
    private Boolean enableUpdate;

    /**
     * 是否显示答案及解析
     */
    @ApiModelProperty(name = "answerAnalysis", value = "是否显示答案及解析", required = true, dataType = "Boolean")
    private Boolean answerAnalysis;

    /**
     * 随机标签列表
     */
    @ApiModelProperty(name = "tagIds", value = "随机标签列表", required = true, dataType = "List<Integer>")
    private String tagIds;

    /**
     * 随机题数
     */
    @ApiModelProperty(name = "questionNum", value = "随机题数", required = true, dataType = "Integer")
    private Integer questionNum;

    /**
     * 随机题型
     */
    @ApiModelProperty(name = "types", value = "随机题型", required = true, dataType = "List<Integer>")
    private String types;

    /**
     * 开始时间
     */
    @ApiModelProperty(name = "startTime", value = "开始时间", required = true, dataType = "Date")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(name = "endTime", value = "结束时间", required = true, dataType = "Date")
    private LocalDateTime endTime;
}
