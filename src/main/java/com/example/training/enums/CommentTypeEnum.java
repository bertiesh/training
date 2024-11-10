package com.example.training.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Xuxinyuan
 */
@Getter
@AllArgsConstructor
public enum CommentTypeEnum {
    /**
     * 文章评论
     */
    ARTICLE(1, "文章评论", "/articles/"),
    /**
     * 文档评论
     */
    FILE(2, "文档评论", "/files/"),
    /**
     * 说说评论
     */
    TALK(3, "讨论评论", "/talks/");

    /**
     * 状态
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 路径
     */
    private final String path;

    /**
     * 获取评论路径
     *
     * @param type 类型
     * @return {@link String}
     */
    public static String getCommentPath(Integer type) {
        for (CommentTypeEnum value : CommentTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value.getPath();
            }
        }
        return null;
    }

    /**
     * 获取评论枚举
     *
     * @param type 类型
     * @return {@link CommentTypeEnum}
     */
    public static CommentTypeEnum getCommentEnum(Integer type) {
        for (CommentTypeEnum value : CommentTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
