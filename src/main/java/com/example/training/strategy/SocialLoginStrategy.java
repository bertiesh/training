package com.example.training.strategy;

import com.example.training.dto.UserInfoDTO;

/**
 * 第三方登录
 * @author Xuxinyuan
 */
public interface SocialLoginStrategy {
    /**
     * 登录
     *
     * @param data 数据
     * @return {@link UserInfoDTO} 用户信息
     */
    UserInfoDTO login(String data);
}
