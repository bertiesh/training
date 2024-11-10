package com.example.training.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.constant.RedisPrefixConst;
import com.example.training.dao.CommentDao;
import com.example.training.dao.TalkDao;
import com.example.training.dto.CommentCountDTO;
import com.example.training.dto.TalkBackDTO;
import com.example.training.dto.TalkDTO;
import com.example.training.entity.Message;
import com.example.training.entity.Talk;
import com.example.training.exception.BizException;
import com.example.training.service.MessageService;
import com.example.training.service.RedisService;
import com.example.training.service.TalkService;
import com.example.training.util.*;
import com.example.training.vo.ConditionVO;
import com.example.training.vo.PageResult;
import com.example.training.vo.TalkVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.training.enums.ArticleStatusEnum.PUBLIC;
import static com.example.training.enums.CommentTypeEnum.TALK;

/**
 * @author Xuxinyuan
 */
@Service
public class TalkServiceImpl extends ServiceImpl<TalkDao, Talk> implements TalkService {
    @Autowired
    private TalkDao talkDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MessageService messageService;

    @Override
    public List<String> listHomeTalks() {
        // 查询最新10条讨论
        return talkDao.selectList(new LambdaQueryWrapper<Talk>()
                        .eq(Talk::getStatus, PUBLIC.getStatus())
                        .orderByDesc(Talk::getIsTop)
                        .orderByDesc(Talk::getId)
                        .last("limit 10"))
                .stream()
                .map(item -> item.getContent().length() > 300 ? HTMLUtils.deleteHMTLTag(item.getContent().substring(0, 300))
                        : HTMLUtils.deleteHMTLTag(item.getContent())).collect(Collectors.toList());
    }

    @Override
    public PageResult<TalkDTO> listTalks(ConditionVO condition) {
        // 查询讨论总量
        int count = Math.toIntExact(talkDao.selectCount((new LambdaQueryWrapper<Talk>()
                .eq(Talk::getStatus, PUBLIC.getStatus())
                .like(Objects.nonNull(condition.getKeywords()), Talk::getContent, condition.getKeywords())
                .eq(Objects.nonNull(condition.getUserInfoId()), Talk::getUserId, condition.getUserInfoId()))));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询讨论
        List<TalkDTO> talkDTOList = talkDao.listTalks(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        // 查询讨论评论量
        List<Integer> talkIdList = talkDTOList.stream()
                .map(TalkDTO::getId)
                .collect(Collectors.toList());
        Map<Integer, Integer> commentCountMap = commentDao.listCommentCountByTopicIds(talkIdList, TALK.getType())
                .stream()
                .collect(Collectors.toMap(CommentCountDTO::getId, CommentCountDTO::getCommentCount));
        // 查询讨论点赞量
        Map<String, Object> likeCountMap = redisService.hGetAll(RedisPrefixConst.TALK_LIKE_COUNT);
        // 查询是否点赞
        String talkLikeKey = RedisPrefixConst.TALK_USER_LIKE + UserUtils.getLoginUser().getUserInfoId();
        talkDTOList.forEach(item -> {
            item.setLikeCount((Integer) likeCountMap.get(item.getId().toString()));
            item.setCommentCount(commentCountMap.get(item.getId()));
            item.setIsLike(redisService.sIsMember(talkLikeKey, item.getId()));
            // 转换图片格式
            if (Objects.nonNull(item.getImages())) {
                item.setImgList(CommonUtils.castList(JSON.parseObject(item.getImages(), List.class), String.class));
            }
        });
        return new PageResult<>(talkDTOList, count);
    }

    @Override
    public TalkDTO getTalkById(Integer talkId) {
        // 查询讨论信息
        TalkDTO talkDTO = talkDao.getTalkById(talkId);
        if (Objects.isNull(talkDTO)) {
            throw new BizException("讨论不存在");
        }
        // 查询讨论点赞量
        talkDTO.setLikeCount((Integer) redisService.hGet(RedisPrefixConst.TALK_LIKE_COUNT, talkId.toString()));
        // 转换图片格式
        if (Objects.nonNull(talkDTO.getImages())) {
            talkDTO.setImgList(CommonUtils.castList(JSON.parseObject(talkDTO.getImages(), List.class), String.class));
        }
        // 判断是否点赞
        String talkLikeKey = RedisPrefixConst.TALK_USER_LIKE + UserUtils.getLoginUser().getUserInfoId();
        talkDTO.setIsLike(redisService.sIsMember(talkLikeKey, talkId));
        return talkDTO;
    }

    @Override
    public void saveTalkLike(Integer talkId) {
        // 判断是否点赞
        String talkLikeKey = RedisPrefixConst.TALK_USER_LIKE + UserUtils.getLoginUser().getUserInfoId();
        if (redisService.sIsMember(talkLikeKey, talkId)) {
            // 点过赞则删除讨论id
            redisService.sRemove(talkLikeKey, talkId);
            // 讨论点赞量-1
            redisService.hDecr(RedisPrefixConst.TALK_LIKE_COUNT, talkId.toString(), 1L);
        } else {
            // 未点赞则增加讨论id
            redisService.sAdd(talkLikeKey, talkId);
            // 讨论点赞量+1
            redisService.hIncr(RedisPrefixConst.TALK_LIKE_COUNT, talkId.toString(), 1L);
            Message message = new Message();
            message.setFromId(1);
            message.setToId(talkDao.selectById(talkId).getUserId());
            message.setFromUserId(UserUtils.getLoginUser().getUserInfoId());
            message.setCommentedId(talkId);
            message.setCommentedContent(talkDao.selectById(talkId).getContent());
            message.setMessageContent("讨论");
            message.setConversationCode("我收到的点赞");
            messageService.saveOrUpdate(message);
        }
    }

    @Override
    public Integer saveOrUpdateTalk(TalkVO talkVO) {
        //过滤讨论
        if (Objects.nonNull(talkVO.getContent())) {
            talkVO.setContent(HTMLUtils.filter(talkVO.getContent()));
        }
        Talk talk = BeanCopyUtils.copyObject(talkVO, Talk.class);
        talk.setUserId(UserUtils.getLoginUser().getUserInfoId());
        this.saveOrUpdate(talk);
        return talk.getId();
    }

    @Override
    public void deleteTalks(List<Integer> talkIdList) {
        talkDao.deleteBatchIds(talkIdList);
    }

    @Override
    public PageResult<TalkBackDTO> listBackTalks(ConditionVO conditionVO) {
        // 查询讨论总量
        int count = Math.toIntExact(talkDao.selectCount(new LambdaQueryWrapper<Talk>()
                .eq(Objects.nonNull(conditionVO.getStatus()), Talk::getStatus, conditionVO.getStatus())));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询讨论
        List<TalkBackDTO> talkDTOList = talkDao.listBackTalks(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVO);
        talkDTOList.forEach(item -> {
            // 转换图片格式
            if (Objects.nonNull(item.getImages())) {
                item.setImgList(CommonUtils.castList(JSON.parseObject(item.getImages(), List.class), String.class));
            }
        });
        return new PageResult<>(talkDTOList, count);
    }

    @Override
    public TalkBackDTO getBackTalkById(Integer talkId) {
        TalkBackDTO talkBackDTO = talkDao.getBackTalkById(talkId);
        // 转换图片格式
        if (Objects.nonNull(talkBackDTO.getImages())) {
            talkBackDTO.setImgList(CommonUtils.castList(JSON.parseObject(talkBackDTO.getImages(), List.class), String.class));
        }
        return talkBackDTO;
    }
}
