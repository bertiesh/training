package com.example.training.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件路径枚举
 * @author Xuxinyuan
 */
@Getter
@AllArgsConstructor
public enum FilePathEnum {
    /**
     * 头像路径
     */
    AVATAR("avatar/", "头像路径"),
    /**
     * 文章图片路径
     */
    ARTICLE("articles/", "文章图片路径"),
    /**
     * 音频路径
     */
    VOICE("voice/", "音频路径"),
    /**
     * 照片路径
     */
    PHOTO("photos/", "相册路径"),
    /**
     * 文档路径
     */
    FILE("files/", "文档路径"),
    /**
     * 文档路径
     */
    LIVE("lives/", "直播路径"),
    /**
     * 题目路径
     */
    QUESTION("questions/", "题目路径"),
    /**
     * 配置图片路径
     */
    CONFIG("config/", "配置图片路径"),
    /**
     * 说说图片路径
     */
    TALK("talks/", "说说图片路径"),
    /**
     * md文件路径
     */
    MD("markdown/", "md文件路径");

    /**
     * 路径
     */
    private final String path;

    /**
     * 描述
     */
    private final String desc;
}
