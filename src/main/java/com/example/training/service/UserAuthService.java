package com.example.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.training.dto.UserAreaDTO;
import com.example.training.dto.UserBackDTO;
import com.example.training.dto.UserInfoDTO;
import com.example.training.entity.UserAuth;
import com.example.training.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Xuxinyuan
 */
@Service
public interface UserAuthService extends IService<UserAuth> {
    /**
     * 发送邮箱验证码
     *
     * @param username 邮箱号
     */
    void sendCode(String username);
    /**
     * 获取用户区域分布
     *
     * @return {@link List <UserAreaDTO>} 用户区域分布
     */
    List<UserAreaDTO> listUserAreas();

    /**
     * 用户注册
     *
     * @param user 用户对象
     */
    void register(UserVO user);

    /**
     * qq登录
     *
     * @param qqLoginVO qq登录信息
     * @return 用户登录信息
     */
    UserInfoDTO qqLogin(QQLoginVO qqLoginVO);

    /**
     * 微博登录
     *
     * @param weiboLoginVO 微博登录信息
     * @return 用户登录信息
     */
    UserInfoDTO weiboLogin(WeiboLoginVO weiboLoginVO);

    /**
     * 修改密码
     *
     * @param user 用户对象
     */
    void updatePassword(UserVO user);

    /**
     * 修改管理员密码
     *
     * @param passwordVO 密码对象
     */
    void updateAdminPassword(PasswordVO passwordVO);

    /**
     * 查询后台用户列表
     *
     * @param condition 条件
     * @return 用户列表
     */
    PageResult<UserBackDTO> listUserBackDTO(ConditionVO condition);

    /**
     * 用户导入
     *
     * @param file 用户信息表
     * @throws IOException IO异常
     */
    void importUsers(MultipartFile file) throws IOException;

    /**
     * 获取验证码
     *
     * @param response http响应
     */
    void getCaptcha(HttpServletResponse response);

    /**
     * 验证验证码
     * @param checkCode 验证码
     * @param captchaOwner 拥有人
     */
    Boolean checkCaptchaCode(String checkCode, String captchaOwner);
}
