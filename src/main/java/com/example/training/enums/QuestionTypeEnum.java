package com.example.training.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Xuxinyuan
 */
@Getter
@AllArgsConstructor
public enum QuestionTypeEnum {
    /**
     * 单选
     */
    RADIO(1,"单选"),

    /**
     * 多选
     */
    CHECKBOX(2,"多选"),

    /**
     * 填空
     */
    BLANK(3,"填空"),

    /**
     * 文本
     */
    TEXT(4,"文本"),

    /**
     * 下拉
     */
    SELECT(5,"下拉"),

    /**
     * 级联
     */
    CASCADE(6,"级联"),

    /**
     * 打分
     */
    SCORE(7,"打分"),

    /**
     * 上传
     */
    UPLOAD(8,"上传");

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;
}
