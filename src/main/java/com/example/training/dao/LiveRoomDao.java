package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.dto.FileCollectionBackDTO;
import com.example.training.dto.LiveRoomBackDTO;
import com.example.training.entity.LiveRoom;
import com.example.training.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface LiveRoomDao extends BaseMapper<LiveRoom> {
    /**
     * get backend live room
     *
     * @param current   page
     * @param size      size
     * @param condition condition
     * @return {@link List < FileCollectionBackDTO >} live rooms
     */
    List<LiveRoomBackDTO> listLiveRoomBacks(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);
}
