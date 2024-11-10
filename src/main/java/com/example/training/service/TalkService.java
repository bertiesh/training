package com.example.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.training.dto.TalkBackDTO;
import com.example.training.dto.TalkDTO;
import com.example.training.entity.Talk;
import com.example.training.vo.ConditionVO;
import com.example.training.vo.PageResult;
import com.example.training.vo.TalkVO;

import java.util.List;

/**
 * @author Xuxinyuan
 */
public interface TalkService extends IService<Talk> {
    /**
     * 获取首页讨论列表
     *
     * @return {@link List <String>} 讨论列表
     */
    List<String> listHomeTalks();

    /**
     * 获取讨论列表
     *
     * @param condition 条件信息
     * @return {@link PageResult <TalkDTO>} 讨论列表
     */
    PageResult<TalkDTO> listTalks(ConditionVO condition);

    /**
     * 根据id查看讨论
     *
     * @param talkId 讨论id
     * @return {@link TalkDTO} 讨论信息
     */
    TalkDTO getTalkById(Integer talkId);

    /**
     * 点赞讨论
     *
     * @param talkId 讨论id
     */
    void saveTalkLike(Integer talkId);

    /**
     * 保存或修改讨论
     *
     * @param talkVO 讨论信息
     * @return
     */
    Integer saveOrUpdateTalk(TalkVO talkVO);

    /**
     * 删除讨论
     *
     * @param talkIdList 讨论id列表
     */
    void deleteTalks(List<Integer> talkIdList);

    /**
     * 查看后台讨论
     *
     * @param conditionVO 条件
     * @return {@link PageResult< TalkBackDTO >}
     */
    PageResult<TalkBackDTO> listBackTalks(ConditionVO conditionVO);

    /**
     * 根据id查看后台说说
     *
     * @param talkId 说说id
     * @return {@link TalkBackDTO} 说说信息
     */
    TalkBackDTO getBackTalkById(Integer talkId);
}
