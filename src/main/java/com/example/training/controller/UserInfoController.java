package com.example.training.controller;

import com.example.training.annotation.OptLog;
import com.example.training.dto.UserFollowDTO;
import com.example.training.dto.UserOnlineDTO;
import com.example.training.service.UserInfoService;
import com.example.training.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static com.baomidou.mybatisplus.core.assist.ISqlRunner.UPDATE;

/**
 * @author Xinyuan Xu
 */
@Api(tags = "用户信息模块")
@RestController
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    /**
     * update user info
     *
     * @param userInfoVO user info
     * @return {@link Result <>}
     */
    @ApiOperation(value = "更新用户信息")
    @PutMapping("/users/info")
    public Result<?> updateUserInfo(@Valid @RequestBody UserInfoVO userInfoVO) {
        userInfoService.updateUserInfo(userInfoVO);
        return Result.ok();
    }

    /**
     * update avatar
     *
     * @param file file
     * @return {@link Result<String>} avatar address
     */
    @ApiOperation(value = "更新用户头像")
    @ApiImplicitParam(name = "file", value = "用户头像", required = true, dataType = "MultipartFile")
    @PostMapping("/users/avatar")
    public Result<String> updateUserAvatar(MultipartFile file) {
        return Result.ok(userInfoService.updateUserAvatar(file));
    }

    /**
     * save user email
     *
     * @param emailVO email info
     * @return {@link Result<>}
     */
    @ApiOperation(value = "绑定用户邮箱")
    @PostMapping("/users/email")
    public Result<?> saveUserEmail(@Valid @RequestBody EmailVO emailVO) {
        userInfoService.saveUserEmail(emailVO);
        return Result.ok();
    }

    /**
     * change user role
     *
     * @param userRoleVO user role info
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改用户角色")
    @PutMapping("/admin/users/role")
    public Result<?> updateUserRole(@Valid @RequestBody UserRoleVO userRoleVO) {
        userInfoService.updateUserRole(userRoleVO);
        return Result.ok();
    }

    /**
     * update user disable status
     *
     * @param userDisableVO user disable status
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改用户禁用状态")
    @PutMapping("/admin/users/disable")
    public Result<?> updateUserDisable(@Valid @RequestBody UserDisableVO userDisableVO) {
        userInfoService.updateUserDisable(userDisableVO);
        return Result.ok();
    }

    /**
     * get online users
     *
     * @param conditionVO condition
     * @return {@link Result< UserOnlineDTO >} online user list
     */
    @ApiOperation(value = "查看在线用户")
    @GetMapping("/admin/users/online")
    public Result<PageResult<UserOnlineDTO>> listOnlineUsers(ConditionVO conditionVO) {
        return Result.ok(userInfoService.listOnlineUsers(conditionVO));
    }

    /**
     * remove online user
     *
     * @param userInfoId user info
     * @return {@link Result<>}
     */
    @ApiOperation(value = "下线用户")
    @DeleteMapping("/admin/users/{userInfoId}/online")
    public Result<?> removeOnlineUser(@PathVariable("userInfoId") Integer userInfoId) {
        userInfoService.removeOnlineUser(userInfoId);
        return Result.ok();
    }

    /**
     * get follows
     *
     * @param conditionVO condition
     * @return {@link Result< UserFollowDTO >} follow list
     */
    @ApiOperation(value = "查看关注用户")
    @GetMapping("/users/follow")
        public Result<PageResult<UserFollowDTO>> listFollowUsers(ConditionVO conditionVO) {
        return Result.ok(userInfoService.listFollows(conditionVO));
    }

    /**
     * get followers
     *
     * @param conditionVO condition
     * @return {@link Result< UserFollowDTO >} follower list
     */
    @ApiOperation(value = "查看被关注用户")
    @GetMapping("/users/followed")
    public Result<PageResult<UserFollowDTO>> listFollowedUsers(ConditionVO conditionVO) {
        return Result.ok(userInfoService.listFollowed(conditionVO));
    }
}
