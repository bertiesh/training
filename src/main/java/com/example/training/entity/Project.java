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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "tb_project")
public class Project {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 问题id表
     */
    private String questions;

    /**
     * 级联情况
     */
    private String cascades;

    /**
     * 模板模式 1 问卷 2 练习
     */
    private Integer status;

    /**
     * 描述
     */
    private String description;

    /**
     * 封面
     */
    private String cover;

    /**
     * 是否删除
     */
    private Boolean isDelete;

    /**
     * 是否开启
     */
    private Boolean isActive;

    /**
     * 是否随机
     */
    private Boolean isRandom;

    /**
     * 是否显示题号
     */
    private Boolean questionNumber;

    /**
     * 密码
     */
    private String password;

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
     * 随机标签列表
     */
    private String tagIds;

    /**
     * 随机题数
     */
    private Integer questionNum;

    /**
     * 随机题型
     */
    private String types;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

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
