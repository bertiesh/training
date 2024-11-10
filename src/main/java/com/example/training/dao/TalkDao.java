package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.dto.TalkBackDTO;
import com.example.training.dto.TalkDTO;
import com.example.training.entity.Talk;
import com.example.training.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface TalkDao extends BaseMapper<Talk> {
    /**
     * list talks
     *
     * @param current page
     * @param size    size
     * @return {@link List <TalkDTO>}
     */
    List<TalkDTO> listTalks(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

    /**
     * list backend talks
     *
     * @param current page
     * @param size    size
     * @param condition condition
     * @return {@link List<TalkBackDTO>}
     */
    List<TalkBackDTO> listBackTalks(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);


    /**
     * get talk by id
     *
     * @param talkId talk id
     * @return {@link TalkDTO} talk
     */
    TalkDTO getTalkById(@Param("talkId") Integer talkId);


    /**
     * get backend talk by id
     *
     * @param talkId talk id
     * @return {@link TalkBackDTO} talk
     */
    TalkBackDTO getBackTalkById(@Param("talkId") Integer talkId);
}
