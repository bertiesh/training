package com.example.training.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.constant.RedisPrefixConst;
import com.example.training.dao.FollowDao;
import com.example.training.dao.UserAuthDao;
import com.example.training.dao.UserInfoDao;
import com.example.training.dto.UserDetailDTO;
import com.example.training.dto.UserFollowDTO;
import com.example.training.dto.UserOnlineDTO;
import com.example.training.entity.*;
import com.example.training.enums.FilePathEnum;
import com.example.training.exception.BizException;
import com.example.training.service.*;
import com.example.training.strategy.context.UploadStrategyContext;
import com.example.training.util.BeanCopyUtils;
import com.example.training.util.UserUtils;
import com.example.training.vo.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.training.util.PageUtils.getLimitCurrent;
import static com.example.training.util.PageUtils.getSize;

/**
 * @author Xuxinyuan
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao, UserInfo> implements UserInfoService {
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private UserAuthDao userAuthDao;
    @Autowired
    private FollowDao followDao;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private RedisService redisService;
    @Autowired
    private FollowService followService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UploadStrategyContext uploadStrategyContext;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserInfo(UserInfoVO userInfoVO) {
        // 封装用户信息
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId())
                .nickname(userInfoVO.getNickname())
                .intro(userInfoVO.getIntro())
                .phone(userInfoVO.getPhone())
                .hospital(userInfoVO.getHospital())
                .qualifications(userInfoVO.getQualifications())
                .build();
        userInfoDao.updateById(userInfo);
        // 更新关注列表
        if (Objects.nonNull(userInfoVO.getUserIdList())) {
            if (Objects.nonNull(UserUtils.getLoginUser().getUserInfoId())) {
                followService.remove(new LambdaQueryWrapper<Follow>()
                        .eq(Follow::getUserInfoId, UserUtils.getLoginUser().getUserInfoId()));
            }
            List<Follow> followList = userInfoVO.getUserIdList().stream()
                    .map(userInfoId -> Follow.builder()
                            .userInfoId(userInfo.getId())
                            .followId(userInfoId)
                            .build())
                    .collect(Collectors.toList());
            followService.saveBatch(followList);
        }
        //新增关注
        if (Objects.nonNull(userInfoVO.getFollowIdList())) {
            for (Integer integer : userInfoVO.getFollowIdList()) {
                Follow follow = new Follow();
                follow.setUserInfoId(UserUtils.getLoginUser().getUserInfoId());
                follow.setFollowId(integer);
                followService.save(follow);
                Message message = new Message();
                message.setFromId(1);
                message.setToId(integer);
                message.setMessageContent(userInfoDao.selectById(UserUtils.getLoginUser().getUserInfoId()).getNickname() + "关注了你");
                message.setFromUserId(UserUtils.getLoginUser().getUserInfoId());
                message.setConversationCode("我收到的关注");
                messageService.saveOrUpdate(message);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String updateUserAvatar(MultipartFile file) {
        // 头像上传
        String avatar = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.AVATAR.getPath());
        // 更新用户信息
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId())
                .avatar(avatar)
                .build();
        userInfoDao.updateById(userInfo);
        return avatar;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveUserEmail(EmailVO emailVO) {
        if (!emailVO.getCode().equals(redisService.get(RedisPrefixConst.USER_CODE_KEY + emailVO.getEmail()).toString())) {
            throw new BizException("验证码错误！");
        }
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId())
                .email(emailVO.getEmail())
                .build();
        userInfoDao.updateById(userInfo);
        UserAuth userAuth = UserAuth.builder().id(UserUtils.getLoginUser().getId()).loginType(1).build();
        userAuthDao.updateById(userAuth);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserRole(UserRoleVO userRoleVO) {
        // 更新用户角色和昵称
        UserInfo userInfo = UserInfo.builder()
                .id(userRoleVO.getUserInfoId())
                .nickname(userRoleVO.getNickname())
                .build();
        userInfoDao.updateById(userInfo);
        // 删除用户角色重新添加
        userRoleService.remove(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userRoleVO.getUserInfoId()));
        List<UserRole> userRoleList = userRoleVO.getRoleIdList().stream()
                .map(roleId -> UserRole.builder()
                        .roleId(roleId)
                        .userId(userRoleVO.getUserInfoId())
                        .build())
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserDisable(UserDisableVO userDisableVO) {
        // 更新用户禁用状态
        UserInfo userInfo = UserInfo.builder()
                .id(userDisableVO.getId())
                .isDisable(userDisableVO.getIsDisable())
                .build();
        userInfoDao.updateById(userInfo);
    }

    @Override
    public PageResult<UserOnlineDTO> listOnlineUsers(ConditionVO conditionVO) {
        // 获取security在线session
        List<UserOnlineDTO> userOnlineDTOList = sessionRegistry.getAllPrincipals().stream()
                .filter(item -> sessionRegistry.getAllSessions(item, false).size() > 0)
                .map(item -> JSON.parseObject(JSON.toJSONString(item), UserOnlineDTO.class))
                .filter(item -> StringUtils.isBlank(conditionVO.getKeywords()) || item.getNickname().contains(conditionVO.getKeywords()))
                .sorted(Comparator.comparing(UserOnlineDTO::getLastLoginTime).reversed())
                .collect(Collectors.toList());
        // 执行分页
        int fromIndex = getLimitCurrent().intValue();
        int size = getSize().intValue();
        int toIndex = userOnlineDTOList.size() - fromIndex > size ? fromIndex + size : userOnlineDTOList.size();
        List<UserOnlineDTO> userOnlineList = userOnlineDTOList.subList(fromIndex, toIndex);
        return new PageResult<>(userOnlineList, userOnlineDTOList.size());
    }

    @Override
    public void removeOnlineUser(Integer userInfoId) {
        // 获取用户session
        List<Object> userInfoList = sessionRegistry.getAllPrincipals().stream().filter(item -> {
            UserDetailDTO userDetailDTO = (UserDetailDTO) item;
            return userDetailDTO.getUserInfoId().equals(userInfoId);
        }).collect(Collectors.toList());
        List<SessionInformation> allSessions = new ArrayList<>();
        userInfoList.forEach(item -> allSessions.addAll(sessionRegistry.getAllSessions(item, false)));
        // 注销session
        allSessions.forEach(SessionInformation::expireNow);
    }

    @Override
    public PageResult<UserFollowDTO> listFollows(ConditionVO conditionVO) {
        // 获取关注的人
        List<UserFollowDTO> userFollowDTOList = new ArrayList<>();
        followDao.selectList(new LambdaQueryWrapper<Follow>().eq(Follow::getUserInfoId, conditionVO.getUserInfoId()))
                .forEach(item -> {
                   userFollowDTOList.add(BeanCopyUtils.copyObject(userInfoDao.selectById(item.getFollowId()), UserFollowDTO.class));
        });
        // 执行分页
        return getUserFollowDTOPageResult(userFollowDTOList, conditionVO);
    }

    @Override
    public PageResult<UserFollowDTO> listFollowed(ConditionVO conditionVO) {
        // 获取关注的人
        List<UserFollowDTO> userFollowDTOList = new ArrayList<>();
        followDao.selectList(new LambdaQueryWrapper<Follow>().eq(Follow::getFollowId, conditionVO.getUserInfoId()))
                .forEach(item -> {
                    userFollowDTOList.add(BeanCopyUtils.copyObject(userInfoDao.selectById(item.getUserInfoId()), UserFollowDTO.class));
                });
        // 执行分页
        return getUserFollowDTOPageResult(userFollowDTOList, conditionVO);
    }

    @NotNull
    private PageResult<UserFollowDTO> getUserFollowDTOPageResult(List<UserFollowDTO> userFollowDTOList, ConditionVO conditionVO) {
        int fromIndex = getLimitCurrent().intValue();
        int size = getSize().intValue();
        int toIndex = userFollowDTOList.size() - fromIndex > size ? fromIndex + size : userFollowDTOList.size();
        List<UserFollowDTO> userFollowDTOS = userFollowDTOList.subList(fromIndex, toIndex);
        return new PageResult<>(userFollowDTOS, userFollowDTOList.size());
    }
}
