package com.example.training.controller;

import com.example.training.annotation.AccessLimit;
import com.example.training.dto.UserAreaDTO;
import com.example.training.dto.UserBackDTO;
import com.example.training.service.UserAuthService;
import com.example.training.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Api(tags = "用户账号模块")
@RestController
public class UserAuthController {
    @Autowired
    private UserAuthService userAuthService;

    /**
     * send email code
     *
     * @param username 用户名
     * @return {@link Result <>}
     */
    @AccessLimit(seconds = 60, maxCount = 1)
    @ApiOperation(value = "发送邮箱验证码")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @GetMapping("/users/code")
    public Result<?> sendCode(String username) {
        userAuthService.sendCode(username);
        return Result.ok();
    }

    /**
     * get user area
     *
     * @return {@link Result<UserAreaDTO>} user area
     */
    @ApiOperation(value = "获取用户区域分布")
    @GetMapping("/admin/users/area")
    public Result<List<UserAreaDTO>> listUserAreas() {
        return Result.ok(userAuthService.listUserAreas());
    }

    /**
     * get backend user list
     *
     * @param condition condition
     * @return {@link Result<UserBackDTO>} user list
     */
    @ApiOperation(value = "查询后台用户列表")
    @GetMapping("/admin/users")
    public Result<PageResult<UserBackDTO>> listUsers(ConditionVO condition) {
        return Result.ok(userAuthService.listUserBackDTO(condition));
    }

    /**
     * user register
     *
     * @param user user info
     * @return {@link Result<>}
     */
    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody UserVO user) {
        userAuthService.register(user);
        return Result.ok();
    }

    /**
     * change password
     *
     * @param user user info
     * @return {@link Result<>}
     */
    @ApiOperation(value = "修改密码")
    @PutMapping("/users/password")
    public Result<?> updatePassword(@Valid @RequestBody UserVO user) {
        userAuthService.updatePassword(user);
        return Result.ok();
    }

    /**
     * admin change password
     *
     * @param passwordVO password info
     * @return {@link Result<>}
     */
    @ApiOperation(value = "修改管理员密码")
    @PutMapping("/admin/users/password")
    public Result<?> updateAdminPassword(@Valid @RequestBody PasswordVO passwordVO) {
        userAuthService.updateAdminPassword(passwordVO);
        return Result.ok();
    }

    /**
     * import users
     *
     * @param file user file
     * @return {@link Result<>}
     */
    @ApiOperation(value = "导入用户表")
    @ApiImplicitParam(name = "file", value = "用户表", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/users/import")
    public Result<?> importUsers(MultipartFile file) throws IOException {
        userAuthService.importUsers(file);
        return Result.ok();
    }

    /**
     * get captcha
     *
     * @param response http response
     */
    @ApiOperation(value = "获取验证码")
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response) {
        userAuthService.getCaptcha(response);
    }

    /**
     * verify captcha
     *
     * @param checkCode captcha
     * @param captchaOwner owner
     */
    @ApiOperation(value = "验证验证码")
    @GetMapping("/captcha/check")
    public Result<Boolean> checkCaptchaCode(@RequestParam String checkCode, @RequestParam String captchaOwner) {
        return Result.ok(userAuthService.checkCaptchaCode(captchaOwner, checkCode));
    }
}
