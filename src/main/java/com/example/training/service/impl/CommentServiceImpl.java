package com.example.training.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.constant.CommonConst;
import com.example.training.constant.MQPrefixConst;
import com.example.training.constant.RedisPrefixConst;
import com.example.training.dao.*;
import com.example.training.dto.*;
import com.example.training.entity.Comment;
import com.example.training.service.InfoService;
import com.example.training.service.CommentService;
import com.example.training.service.MessageService;
import com.example.training.service.RedisService;
import com.example.training.util.*;
import com.example.training.vo.*;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.example.training.enums.CommentTypeEnum.*;

/**
 * @author Xuxinyuan
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private TalkDao talkDao;
    @Autowired
    private FileDao fileDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private InfoService blogInfoService;

    /**
     * 网站网址
     */
    @Value("${website.url}")
    private String websiteUrl;

    @Override
    public PageResult<CommentDTO> listComments(CommentVO commentVO) {
        // 查询评论量
        int commentCount = Math.toIntExact(commentDao.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Objects.nonNull(commentVO.getTopicId()), Comment::getTopicId, commentVO.getTopicId())
                .eq(Comment::getType, commentVO.getType())
                .isNull(Comment::getParentId)
                .eq(Comment::getIsReview, CommonConst.TRUE)));
        if (commentCount == 0) {
            return new PageResult<>();
        }
        // 分页查询评论数据
        List<CommentDTO> commentDTOList = commentDao.listComments(PageUtils.getLimitCurrent(), PageUtils.getSize(), commentVO);
        if (CollectionUtils.isEmpty(commentDTOList)) {
            return new PageResult<>();
        }
        // 查询redis的评论点赞数据
        Map<String, Object> likeCountMap = redisService.hGetAll(RedisPrefixConst.COMMENT_LIKE_COUNT);
        // 提取评论id集合
        List<Integer> commentIdList = commentDTOList.stream()
                .map(CommentDTO::getId)
                .collect(Collectors.toList());
        // 根据评论id集合查询回复数据
        List<ReplyDTO> replyDTOList = commentDao.listReplies(commentIdList);
        // 封装回复点赞量
        replyDTOList.forEach(item -> {
            item.setLikeCount((Integer) likeCountMap.get(item.getId().toString()));
            item.setIsLike(redisService.sIsMember(RedisPrefixConst.COMMENT_USER_LIKE
                    + UserUtils.getLoginUser().getUserInfoId(), item.getId()));
        });
        // 根据评论id分组回复数据
        Map<Integer, List<ReplyDTO>> replyMap = replyDTOList.stream()
                .collect(Collectors.groupingBy(ReplyDTO::getParentId));
        // 根据评论id查询回复量
        Map<Integer, Integer> replyCountMap = commentDao.listReplyCountByCommentId(commentIdList)
                .stream().collect(Collectors.toMap(ReplyCountDTO::getCommentId, ReplyCountDTO::getReplyCount));
        // 封装评论数据
        commentDTOList.forEach(item -> {
            item.setLikeCount((Integer) likeCountMap.get(item.getId().toString()));
            item.setIsLike(redisService.sIsMember(RedisPrefixConst.COMMENT_USER_LIKE
                    + UserUtils.getLoginUser().getUserInfoId(), item.getId()));
            item.setReplyDTOList(replyMap.get(item.getId()));
            item.setReplyCount(replyCountMap.get(item.getId()));
        });
        return new PageResult<>(commentDTOList, commentCount);
    }

    @Override
    public List<ReplyDTO> listRepliesByCommentId(Integer commentId) {
        // 转换页码查询评论下的回复
        List<ReplyDTO> replyDTOList = commentDao.listRepliesByCommentId(PageUtils.getLimitCurrent(),
                PageUtils.getSize(), commentId);
        // 查询redis的评论点赞数据
        Map<String, Object> likeCountMap = redisService.hGetAll(RedisPrefixConst.COMMENT_LIKE_COUNT);
        // 封装点赞数据
        replyDTOList.forEach(item -> item.setLikeCount((Integer) likeCountMap.get(item.getId().toString())));
        return replyDTOList;
    }

    @Override
    public CommentDTO saveComment(CommentVO commentVO) {
        // 判断是否需要审核
        WebsiteConfigVO websiteConfig = blogInfoService.getWebsiteConfig();
        Integer isReview = websiteConfig.getIsCommentReview();
        // 过滤标签
        commentVO.setCommentContent(HTMLUtils.filter(commentVO.getCommentContent()));
        Comment comment = Comment.builder()
                .userId(UserUtils.getLoginUser().getUserInfoId())
                .replyUserId(commentVO.getReplyUserId())
                .topicId(commentVO.getTopicId())
                .commentContent(commentVO.getCommentContent())
                .parentId(commentVO.getParentId())
                .type(commentVO.getType())
                .isReview(isReview == CommonConst.TRUE ? CommonConst.FALSE : CommonConst.TRUE)
                .build();
        commentDao.insert(comment);
        // 判断是否开启邮箱通知,通知用户
        if (websiteConfig.getIsEmailNotice().equals(CommonConst.TRUE)) {
            CompletableFuture.runAsync(() -> notice(comment));
        }
        com.example.training.entity.Message message = new com.example.training.entity.Message();
        message.setFromId(1);
        message.setToId(commentVO.getReplyUserId());
        message.setFromUserId(UserUtils.getLoginUser().getUserInfoId());
        message.setMessageContent(comment.getCommentContent());
        if (commentVO.getParentId() != null){
            message.setCommentedId(commentVO.getParentId());
            message.setCommentedContent(commentDao.selectById(message.getCommentedId()).getCommentContent());
            message.setOriginalId(comment.getTopicId());
            message.setOriginalType(comment.getType());
            if (message.getOriginalType() == 1) {
                message.setOriginalUserName(userInfoDao.selectById(articleDao.selectById(message.getOriginalId()).getUserId()).getNickname());
                message.setOriginalContent(articleDao.selectById(message.getOriginalId()).getArticleTitle());
            } else if (message.getOriginalType() == 3) {
                message.setOriginalUserName(userInfoDao.selectById(talkDao.selectById(message.getOriginalId()).getUserId()).getNickname());
                message.setOriginalContent(talkDao.selectById(message.getOriginalId()).getContent());
            } else {
                message.setOriginalUserName("innolcon");
                message.setOriginalContent(fileDao.selectById(message.getOriginalId()).getFileName());
            }
            message.setMessageContent("评论");
        }else if (commentVO.getType().equals(TALK.getType())){
            message.setCommentedId(comment.getTopicId());
            message.setCommentedContent(talkDao.selectById(message.getCommentedId()).getContent());
            message.setMessageContent("讨论");
        }else if (commentVO.getType() == 1){
            message.setCommentedId(comment.getTopicId());
            message.setCommentedContent(articleDao.selectById(message.getCommentedId()).getArticleTitle());
            message.setMessageContent("文章");
        }else {
            message.setCommentedId(comment.getTopicId());
            message.setCommentedContent(fileDao.selectById(message.getCommentedId()).getFileName());
            message.setMessageContent("文档");
        }
        message.setConversationCode("我收到的评论");
        messageService.saveOrUpdate(message);
        CommentDTO commentDTO = BeanCopyUtils.copyObject(commentDao.selectById(comment.getId()), CommentDTO.class);
        commentDTO.setAvatar(userInfoDao.selectById(commentDTO.getUserId()).getAvatar());
        commentDTO.setNickname(userInfoDao.selectById(commentDTO.getUserId()).getNickname());
        return commentDTO;
    }

    @Override
    public void saveCommentLike(Integer commentId) {
        // 判断是否点赞
        String commentLikeKey = RedisPrefixConst.COMMENT_USER_LIKE + UserUtils.getLoginUser().getUserInfoId();
        if (redisService.sIsMember(commentLikeKey, commentId)) {
            // 点过赞则删除评论id
            redisService.sRemove(commentLikeKey, commentId);
            // 评论点赞量-1
            redisService.hDecr(RedisPrefixConst.COMMENT_LIKE_COUNT, commentId.toString(), 1L);
        } else {
            // 未点赞则增加评论id
            redisService.sAdd(commentLikeKey, commentId);
            // 评论点赞量+1
            redisService.hIncr(RedisPrefixConst.COMMENT_LIKE_COUNT, commentId.toString(), 1L);
            com.example.training.entity.Message message = new com.example.training.entity.Message();
            message.setFromId(1);
            message.setToId(commentDao.selectById(commentId).getUserId());
            message.setFromUserId(UserUtils.getLoginUser().getUserInfoId());
            message.setCommentedId(commentId);
            message.setCommentedContent(commentDao.selectById(commentId).getCommentContent());
            if (commentDao.selectById(commentId).getType() == 1) {
                message.setOriginalType(1);
                message.setOriginalId(commentDao.selectById(commentId).getTopicId());
                message.setOriginalUserName(userInfoDao.selectById(articleDao.selectById(message.getOriginalId()).getUserId()).getNickname());
                message.setOriginalContent(articleDao.selectById(message.getOriginalId()).getArticleTitle());
            } else if (commentDao.selectById(commentId).getType() == 3) {
                message.setOriginalType(3);
                message.setOriginalId(commentDao.selectById(commentId).getTopicId());
                message.setOriginalUserName(userInfoDao.selectById(talkDao.selectById(message.getOriginalId()).getUserId()).getNickname());
                message.setOriginalContent(talkDao.selectById(message.getOriginalId()).getContent());
            } else {
                message.setOriginalType(2);
                message.setOriginalId(commentDao.selectById(commentId).getTopicId());
                message.setOriginalUserName("innolcon");
                message.setOriginalContent(fileDao.selectById(message.getOriginalId()).getFileName());
            }
            message.setMessageContent("评论");
            message.setConversationCode("我收到的点赞");
            messageService.saveOrUpdate(message);
        }
    }

    @Override
    public void updateCommentsReview(ReviewVO reviewVO) {
        // 修改评论审核状态
        List<Comment> commentList = reviewVO.getIdList().stream().map(item -> Comment.builder()
                        .id(item)
                        .isReview(reviewVO.getIsReview())
                        .build())
                .collect(Collectors.toList());
        this.updateBatchById(commentList);
    }

    @Override
    public PageResult<CommentBackDTO> listCommentBackDTO(ConditionVO condition) {
        // 统计后台评论量
        Integer count = commentDao.countCommentDTO(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台评论集合
        List<CommentBackDTO> commentBackDTOList;
        if (condition.getType() == 1){
            commentBackDTOList = commentDao.listArticleCommentBackDTO(PageUtils.getLimitCurrent(),
                    PageUtils.getSize(), condition);
        }else {
            commentBackDTOList = commentDao.listTalkCommentBackDTO(PageUtils.getLimitCurrent(),
                    PageUtils.getSize(), condition);
        }
        return new PageResult<>(commentBackDTOList, count);
    }

    /**
     * 通知评论用户
     *
     * @param comment 评论信息
     */
    public void notice(Comment comment) {
        // 查询回复用户邮箱号
        Integer userId = CommonConst.BLOGGER_ID;
        String id = Objects.nonNull(comment.getTopicId()) ? comment.getTopicId().toString() : "";
        if (Objects.nonNull(comment.getReplyUserId())) {
            userId = comment.getReplyUserId();
        } else {
            switch (Objects.requireNonNull(getCommentEnum(comment.getType()))) {
                case ARTICLE:
                    userId = articleDao.selectById(comment.getTopicId()).getUserId();
                    break;
                case TALK:
                    userId = talkDao.selectById(comment.getTopicId()).getUserId();
                    break;
                case FILE:
                    userId = 1;
                    break;
                default:
                    break;
            }
        }
        String email = userInfoDao.selectById(userId).getEmail();
        if (StringUtils.isNotBlank(email)) {
            // 发送消息
            EmailDTO emailDTO = new EmailDTO();
            if (comment.getIsReview().equals(CommonConst.TRUE)) {
                // 评论提醒
                emailDTO.setEmail(email);
                emailDTO.setSubject("评论提醒");
                // 获取评论路径
                String url = websiteUrl + getCommentPath(comment.getType()) + id;
                emailDTO.setContent("您收到了一条新的回复，请前往" + url + "\n页面查看");
            } else {
                // 管理员审核提醒
                String adminEmail = userInfoDao.selectById(CommonConst.BLOGGER_ID).getEmail();
                emailDTO.setEmail(adminEmail);
                emailDTO.setSubject("审核提醒");
                emailDTO.setContent("您收到了一条新的回复，请前往后台管理页面审核");
            }
//            rabbitTemplate.convertAndSend(MQPrefixConst.EMAIL_EXCHANGE, "*", new Message(JSON.toJSONBytes(emailDTO),
//                    new MessageProperties()));
        }
    }
}
