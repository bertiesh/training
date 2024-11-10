package com.example.training.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.dao.LiveRoomDao;
import com.example.training.dao.UserInfoDao;
import com.example.training.dto.LiveRoomBackDTO;
import com.example.training.dto.LiveRoomDTO;
import com.example.training.dto.UserInfoDTO;
import com.example.training.entity.LiveRoom;
import com.example.training.exception.BizException;
import com.example.training.service.LiveRoomService;
import com.example.training.util.BeanCopyUtils;
import com.example.training.util.PageUtils;
import com.example.training.vo.ConditionVO;
import com.example.training.vo.LiveRoomVO;
import com.example.training.vo.PageResult;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Xuxinyuan
 */
@Service
public class LiveRoomServiceImpl extends ServiceImpl<LiveRoomDao, LiveRoom> implements LiveRoomService {
    @Autowired
    private LiveRoomDao liveRoomDao;
    @Autowired
    private UserInfoDao userInfoDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateLive(LiveRoomVO liveRoomVO) {
        liveRoomVO.setDescription(StringEscapeUtils.escapeHtml4(liveRoomVO.getDescription()));
        // 查询直播间名是否存在
        LiveRoom liveRoom = liveRoomDao.selectOne(new LambdaQueryWrapper<LiveRoom>().select(LiveRoom::getId)
                .eq(LiveRoom::getName, liveRoomVO.getName()));
        if (Objects.nonNull(liveRoom) && !liveRoom.getId().equals(liveRoomVO.getId())) {
            throw new BizException("直播间名已存在");
        }
        LiveRoom room = BeanCopyUtils.copyObject(liveRoomVO, LiveRoom.class);
        this.saveOrUpdate(room);
    }

    @Override
    public PageResult<LiveRoomBackDTO> listLiveBacks(ConditionVO condition) {
        // 查询直播信息
        List<LiveRoomBackDTO> liveRoomBackDTOList = BeanCopyUtils.copyList(liveRoomDao.selectList(
                new LambdaQueryWrapper<LiveRoom>()
                        .eq(Objects.nonNull(condition.getIsDelete()), LiveRoom::getIsDelete, condition.getIsDelete())
                        .eq(Objects.nonNull(condition.getType()), LiveRoom::getType, condition.getType())
                        .eq(Objects.nonNull(condition.getKeywords()), LiveRoom::getName, condition.getKeywords())
                        .orderByDesc(LiveRoom::getIsTop).orderByDesc(LiveRoom::getStartTime)
                        .orderByDesc(LiveRoom::getId)), LiveRoomBackDTO.class);
        LocalDateTime now = LocalDateTime.now();
        liveRoomBackDTOList.forEach(item -> {
            item.setUserInfoDTO(BeanCopyUtils.copyObject(userInfoDao.selectById(item.getUserInfoId()), UserInfoDTO.class));
            doFilter(item, now);
        });
        if (Objects.nonNull(condition.getStatus())) {
            liveRoomBackDTOList = liveRoomBackDTOList.stream().filter(LiveRoomBackDTO -> LiveRoomBackDTO.getIsLive()
                    .equals(condition.getStatus())).collect(Collectors.toList());
        }
        int count = liveRoomBackDTOList.size();
        if (Objects.nonNull(condition.getCurrent())) {
            if (count > condition.getCurrent() * condition.getSize()) {
                liveRoomBackDTOList = liveRoomBackDTOList.subList((int) ((condition.getCurrent() - 1) * condition.getSize()),
                        (int) (condition.getCurrent() * condition.getSize()));
            } else {
                liveRoomBackDTOList = liveRoomBackDTOList.subList((int) ((condition.getCurrent() - 1) * condition.getSize()),
                        count);
            }
        }
        return new PageResult<>(liveRoomBackDTOList, count);
    }

    @Override
    public LiveRoomBackDTO getLiveBackById(Integer roomId) {
        // 查询直播信息
        LiveRoom liveRoom = liveRoomDao.selectById(roomId);
        LiveRoomBackDTO liveRoomBackDTO = BeanCopyUtils.copyObject(liveRoom, LiveRoomBackDTO.class);
        liveRoomBackDTO.setUserInfoDTO(BeanCopyUtils.copyObject(userInfoDao.selectById(liveRoomBackDTO.getUserInfoId()), UserInfoDTO.class));
        LocalDateTime now = LocalDateTime.now();
        return doFilter(liveRoomBackDTO, now);
    }

    @Override
    public LiveRoomDTO getLiveById(Integer roomId) {
        // 查询直播信息
        LiveRoom liveRoom = liveRoomDao.selectById(roomId);
        LiveRoomDTO liveRoomDTO = BeanCopyUtils.copyObject(liveRoom, LiveRoomDTO.class);
        liveRoomDTO.setUserInfoDTO(BeanCopyUtils.copyObject(userInfoDao.selectById(liveRoomDTO.getUserInfoId()), UserInfoDTO.class));
        return liveRoomDTO;
    }

    private LiveRoomBackDTO doFilter(LiveRoomBackDTO liveRoomBackDTO, LocalDateTime now) {
        if (now.isAfter(liveRoomBackDTO.getStartTime()) && now.isBefore(liveRoomBackDTO.getEndTime())) {
            liveRoomBackDTO.setIsLive(1);
        } else if (now.isBefore(liveRoomBackDTO.getStartTime())){
            liveRoomBackDTO.setIsLive(0);
        } else {
            liveRoomBackDTO.setIsLive(2);
        }
        liveRoomBackDTO.setDescription(StringEscapeUtils.unescapeHtml4(liveRoomBackDTO.getDescription()));
        return liveRoomBackDTO;
    }

    @Override
    public PageResult<LiveRoomDTO> listLives(ConditionVO condition) {
        condition.setIsDelete(0);
        PageResult<LiveRoomBackDTO> live = listLiveBacks(condition);
        PageResult<LiveRoomDTO> pageResult = new PageResult<>();
        pageResult.setRecordList(BeanCopyUtils.copyList(live.getRecordList(), LiveRoomDTO.class));
        pageResult.setCount(live.getCount());
        return pageResult;
    }


}
