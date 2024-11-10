package com.example.training.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.constant.CommonConst;
import com.example.training.constant.MQPrefixConst;
import com.example.training.constant.RedisPrefixConst;
import com.example.training.dao.InvitationCodeDao;
import com.example.training.dao.UserAuthDao;
import com.example.training.dao.UserInfoDao;
import com.example.training.dao.UserRoleDao;
import com.example.training.dto.EmailDTO;
import com.example.training.dto.UserAreaDTO;
import com.example.training.dto.UserBackDTO;
import com.example.training.dto.UserInfoDTO;
import com.example.training.entity.InvitationCode;
import com.example.training.entity.UserAuth;
import com.example.training.entity.UserInfo;
import com.example.training.entity.UserRole;
import com.example.training.enums.LoginTypeEnum;
import com.example.training.enums.RoleEnum;
import com.example.training.exception.BizException;
import com.example.training.service.InfoService;
import com.example.training.service.RedisService;
import com.example.training.service.UserAuthService;
import com.example.training.strategy.context.SocialLoginStrategyContext;
import com.example.training.util.PageUtils;
import com.example.training.util.UserUtils;
import com.example.training.vo.*;
import com.google.code.kaptcha.Producer;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.training.constant.CommonConst.DEFAULT_PASSWORD;
import static com.example.training.enums.UserAreaTypeEnum.getUserAreaType;
import static com.example.training.util.CommonUtils.*;

/**
 * @author Xuxinyuan
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthDao, UserAuth> implements UserAuthService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserAuthDao userAuthDao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private InvitationCodeDao invitationCodeDao;
    @Autowired
    private InfoService blogInfoService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private SocialLoginStrategyContext socialLoginStrategyContext;
    @Autowired
    private Producer producer;

    @Override
    public void sendCode(String username) {
        // 校验账号是否合法
        if (!checkEmail(username)) {
            throw new BizException("请输入正确邮箱");
        }
        // 生成六位随机验证码发送
        String code = getRandomCode();
        // 发送验证码
        EmailDTO emailDTO = EmailDTO.builder()
                .email(username)
                .subject("验证码")
                .content("您的验证码为 " + code + " 有效期15分钟，请不要告诉他人哦！")
                .build();
        rabbitTemplate.convertAndSend(MQPrefixConst.EMAIL_EXCHANGE, "*", new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
        // 将验证码存入redis，设置过期时间为15分钟
        redisService.set(RedisPrefixConst.USER_CODE_KEY + username, code, RedisPrefixConst.CODE_EXPIRE_TIME);
    }

    @Override
    public List<UserAreaDTO> listUserAreas() {
        List<UserAreaDTO> userAreaDTOList = new ArrayList<>();
        switch (Objects.requireNonNull(getUserAreaType(1))) {
            case USER:
                // 查询注册用户区域分布
                Object userArea = redisService.get(RedisPrefixConst.USER_AREA);
                if (Objects.nonNull(userArea)) {
                    userAreaDTOList = JSON.parseObject(userArea.toString(), List.class);
                }
                return userAreaDTOList;
            case VISITOR:
                // 查询游客区域分布
                Map<String, Object> visitorArea = redisService.hGetAll(RedisPrefixConst.VISITOR_AREA);
                if (Objects.nonNull(visitorArea)) {
                    userAreaDTOList = visitorArea.entrySet().stream()
                            .map(item -> UserAreaDTO.builder()
                                    .name(item.getKey())
                                    .value(Long.valueOf(item.getValue().toString()))
                                    .build())
                            .collect(Collectors.toList());
                }
                return userAreaDTOList;
            default:
                break;
        }
        return userAreaDTOList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(UserVO user) {
        // 校验邀请码是否合法
        if (!checkInvitation(user)) {
            throw new BizException("非法的邀请码！");
        }
        // 校验账号是否合法
        if (checkUser(user)) {
            throw new BizException("邮箱已被注册！");
        }
        // 新增用户信息
        UserInfo userInfo = UserInfo.builder()
                .email(user.getUsername())
                .nickname(CommonConst.DEFAULT_NICKNAME + IdWorker.getId())
                .avatar(blogInfoService.getWebsiteConfig().getUserAvatar())
                .build();
        userInfoDao.insert(userInfo);
        // 绑定用户角色
        UserRole userRole = UserRole.builder()
                .userId(userInfo.getId())
                .roleId(RoleEnum.USER.getRoleId())
                .build();
        userRoleDao.insert(userRole);
        // 新增用户账号
        UserAuth userAuth = UserAuth.builder()
                .userInfoId(userInfo.getId())
                .username(user.getUsername())
                .password(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()))
                .loginType(LoginTypeEnum.EMAIL.getType())
                .build();
        userAuthDao.insert(userAuth);
    }

    @Override
    public void updatePassword(UserVO user) {
        // 校验账号是否合法
        if (!checkUser(user)) {
            throw new BizException("邮箱尚未注册！");
        }
        // 根据用户名修改密码
        userAuthDao.update(new UserAuth(), new LambdaUpdateWrapper<UserAuth>()
                .set(UserAuth::getPassword, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()))
                .eq(UserAuth::getUsername, user.getUsername()));
    }

    @Override
    public void updateAdminPassword(PasswordVO passwordVO) {
        // 查询旧密码是否正确
        UserAuth user = userAuthDao.selectOne(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getId, UserUtils.getLoginUser().getId()));
        // 正确则修改密码，错误则提示不正确
        if (Objects.nonNull(user) && BCrypt.checkpw(passwordVO.getOldPassword(), user.getPassword())) {
            UserAuth userAuth = UserAuth.builder()
                    .id(UserUtils.getLoginUser().getId())
                    .password(BCrypt.hashpw(passwordVO.getNewPassword(), BCrypt.gensalt()))
                    .build();
            userAuthDao.updateById(userAuth);
        } else {
            throw new BizException("旧密码不正确");
        }
    }

    @Override
    public PageResult<UserBackDTO> listUserBackDTO(ConditionVO condition) {
        // 获取后台用户数量
        Integer count = userAuthDao.countUser(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 获取后台用户列表
        List<UserBackDTO> userBackDTOList = userAuthDao.listUsers(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        return new PageResult<>(userBackDTOList, count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserInfoDTO qqLogin(QQLoginVO qqLoginVO) {
        return socialLoginStrategyContext.executeLoginStrategy(JSON.toJSONString(qqLoginVO), LoginTypeEnum.QQ);
    }

    @Transactional(rollbackFor = BizException.class)
    @Override
    public UserInfoDTO weiboLogin(WeiboLoginVO weiboLoginVO) {
        return socialLoginStrategyContext.executeLoginStrategy(JSON.toJSONString(weiboLoginVO), LoginTypeEnum.WEIBO);
    }

    /**
     * 校验用户数据是否合法
     *
     * @param user 用户数据
     * @return 结果
     */
    private Boolean checkUser(UserVO user) {
        if (!user.getCode().equals(redisService.get(RedisPrefixConst.USER_CODE_KEY + user.getUsername()))) {
            throw new BizException("验证码错误！");
        }
        //查询用户名是否存在
        UserAuth userAuth = userAuthDao.selectOne(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getUsername)
                .eq(UserAuth::getUsername, user.getUsername()));
        return Objects.nonNull(userAuth);
    }

    /**
     * 校验用户邀请码是否合法
     *
     * @param user 用户数据
     * @return 结果
     */
    private boolean checkInvitation(UserVO user) {
        InvitationCode invitationCode = invitationCodeDao.selectOne(new LambdaQueryWrapper<InvitationCode>()
                .eq(InvitationCode::getInvitationCode, user.getInvitationCode()));
        return Objects.nonNull(invitationCode);
    }

    /**
     * 统计用户地区
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void statisticalUserArea() {
        // 统计用户地域分布
        Map<String, Long> userAreaMap = userAuthDao.selectList(new LambdaQueryWrapper<UserAuth>().select(UserAuth::getIpSource))
                .stream()
                .map(item -> {
                    if (StringUtils.isNotBlank(item.getIpSource())) {
                        return item.getIpSource().substring(0, 2)
                                .replaceAll(CommonConst.PROVINCE, "")
                                .replaceAll(CommonConst.CITY, "");
                    }
                    return CommonConst.UNKNOWN;
                })
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()));
        // 转换格式
        List<UserAreaDTO> userAreaList = userAreaMap.entrySet().stream()
                .map(item -> UserAreaDTO.builder()
                        .name(item.getKey())
                        .value(item.getValue())
                        .build())
                .collect(Collectors.toList());
        redisService.set(RedisPrefixConst.USER_AREA, JSON.toJSONString(userAreaList));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importUsers(MultipartFile file) throws IOException {
        //获取excel表
        InputStream inputStream = file.getInputStream();
        ReadableWorkbook readableWorkbook = new ReadableWorkbook(inputStream);
        readableWorkbook.getSheets().forEach(sheet -> {
            try {
                Stream<Row> rows = sheet.openStream();
                rows.forEach(row -> {
                    if (row.getRowNum() == 1) {
                        return;
                    }
                    // 新增用户信息
                    if (getCellValue(row, 0).orElse(null) != null &&
                            userAuthDao.selectCount(new LambdaQueryWrapper<UserAuth>().select(UserAuth::getUsername)
                            .eq(UserAuth::getUsername, getCellValue(row, 0).orElse(null))) == 0){
                        UserInfo userInfo = UserInfo.builder()
                                .email(getCellValue(row, 0).orElse(null))
                                .nickname(getCellValue(row, 1).orElse(CommonConst.DEFAULT_NICKNAME + IdWorker.getId()))
                                .avatar(blogInfoService.getWebsiteConfig().getUserAvatar())
                                .intro(getCellValue(row, 2).orElse(null))
                                .phone(getCellValue(row, 3).orElse(null))
                                .hospital(getCellValue(row, 4).orElse(null))
                                .qualifications(getCellValue(row, 5).orElse(null))
                                .build();
                        userInfoDao.insert(userInfo);
                        // 绑定用户角色
                        UserRole userRole = UserRole.builder()
                                .userId(userInfo.getId())
                                .roleId(RoleEnum.USER.getRoleId())
                                .build();
                        userRoleDao.insert(userRole);
                        // 新增用户账号
                        UserAuth userAuth = UserAuth.builder()
                                .userInfoId(userInfo.getId())
                                .username(userInfo.getEmail())
                                .password(BCrypt.hashpw(blogInfoService.getWebsiteConfig().getDefaultPassword(), BCrypt.gensalt()))
                                .loginType(4)
                                .build();
                        userAuthDao.insert(userAuth);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Optional<String> getCellValue(Row row, int cellIndex) {
        String cellValue = row.getCellAsString(cellIndex).orElse(null);
        if (StringUtils.isBlank(cellValue)) {
            return Optional.empty();
        }
        return Optional.of(cellValue);
    }

    @Override
    public void getCaptcha(HttpServletResponse response) {
        // 生成验证码
        // 生成随机字符
        String text = producer.createText();
        // 生成图片
        BufferedImage image = producer.createImage(text);

        // 验证码的归属者
        String captchaOwner = generateUUID();
        Cookie cookie = new Cookie("captchaOwner", captchaOwner);
        cookie.setMaxAge(60);
        cookie.setPath("/");
        response.addCookie(cookie);
        // 将验证码存入 redis
        redisService.set(RedisPrefixConst.USER_CAPTCHA_KEY + captchaOwner, text, RedisPrefixConst.CAPTCHA_EXPIRE_TIME);

        // 将图片输出给浏览器
        response.setContentType("image/png");
        try {
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            log.error("响应验证码失败");
        }
    }

    @Override
    public Boolean checkCaptchaCode(String captchaOwner, String checkCode) {
        if (StringUtils.isBlank(checkCode)) {
            return false;
        }
        if (!checkCode.equals(redisService.get(RedisPrefixConst.USER_CAPTCHA_KEY + captchaOwner))) {
            throw new BizException("验证码错误！");
        }
        return true;
    }
}

