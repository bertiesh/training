package com.example.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.training.dto.LiveRoomBackDTO;
import com.example.training.dto.LiveRoomDTO;
import com.example.training.entity.LiveRoom;
import com.example.training.vo.ConditionVO;
import com.example.training.vo.LiveRoomVO;
import com.example.training.vo.PageResult;
import com.example.training.vo.Result;

/**
 * @author Xuxinyuan
 */
public interface LiveRoomService extends IService<LiveRoom> {
    /**
     * 保存或更新直播间
     *
     * @param liveRoomVO 直播间信息
     */
    void saveOrUpdateLive(LiveRoomVO liveRoomVO);

    /**
     * 查看后台直播间列表
     *
     * @param condition 条件
     * @return {@link Result<LiveRoomBackDTO>} 直播间列表
     */
    PageResult<LiveRoomBackDTO> listLiveBacks(ConditionVO condition);

    /**
     * 根据id获取后台直播间信息
     * @param roomId 直播间id
     * @return {@link Result}直播间信息
     */
    LiveRoomBackDTO getLiveBackById(Integer roomId);

    /**
     * 根据id获取直播信息
     * @param roomId 直播间id
     * @return {@link Result}直播信息
     */
    LiveRoomDTO getLiveById(Integer roomId);

    /**
     * 获取直播间列表
     * @param condition 条件
     * @return {@link Result<LiveRoomDTO>} 直播间列表
     */
    PageResult<LiveRoomDTO> listLives(ConditionVO condition);
}
