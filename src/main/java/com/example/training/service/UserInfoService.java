package com.example.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.training.dto.UserFollowDTO;
import com.example.training.dto.UserOnlineDTO;
import com.example.training.entity.UserInfo;
import com.example.training.vo.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Xuxinyuan
 */
public interface UserInfoService extends IService<UserInfo> {
    /**
     * 修改用户资料
     *
     * @param userInfoVO 用户资料
     */
    void updateUserInfo(UserInfoVO userInfoVO);

    /**
     * 修改用户头像
     *
     * @param file 头像图片
     * @return 头像地址
     */
    String updateUserAvatar(MultipartFile file);

    /**
     * 绑定用户邮箱
     *
     * @param emailVO 邮箱
     */
    void saveUserEmail(EmailVO emailVO);

    /**
     * 更新用户角色
     *
     * @param userRoleVO 更新用户角色
     */
    void updateUserRole(UserRoleVO userRoleVO);

    /**
     * 修改用户禁用状态
     *
     * @param userDisableVO 用户禁用信息
     */
    void updateUserDisable(UserDisableVO userDisableVO);

    /**
     * 查看在线用户列表
     *
     * @param conditionVO 条件
     * @return 在线用户列表
     */
    PageResult<UserOnlineDTO> listOnlineUsers(ConditionVO conditionVO);

    /**
     * 下线用户
     *
     * @param userInfoId 用户信息id
     */
    void removeOnlineUser(Integer userInfoId);

    /**
     * 查看关注用户
     * @param conditionVO 条件
     * @return {@link Result< UserFollowDTO >} 关注用户列表
     */
    PageResult<UserFollowDTO> listFollows(ConditionVO conditionVO);

    /**
     * 查看被关注用户
     *
     * @param conditionVO 条件
     * @return {@link Result< UserFollowDTO >} 被关注用户列表
     */
    PageResult<UserFollowDTO> listFollowed(ConditionVO conditionVO);
}
