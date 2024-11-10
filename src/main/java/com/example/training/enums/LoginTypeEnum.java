package com.example.training.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Xuxinyuan
 */

@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
    /**
     * 邮箱登录
     */
    EMAIL(1, "邮箱登录", ""),
    /**
     * QQ登录
     */
    QQ(2, "QQ登录", "qqLoginStrategyImpl"),
    /**
     * 微博登录
     */
    WEIBO(3, "微博登录", "weiboLoginStrategyImpl"),
    /**
     * 邮箱没验证
     */
    UNAUTHENTICATED(4, "未验证邮箱", "")
    ;

    /**
     * 登录方式
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 策略
     */
    private final String strategy;
}
