package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.dto.*;
import com.example.training.entity.Comment;
import com.example.training.vo.CommentVO;
import com.example.training.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface CommentDao extends BaseMapper<Comment> {
    /**
     * get comment
     *
     * @param current   page
     * @param size      size
     * @param commentVO comment info
     * @return comment list
     */
    List<CommentDTO> listComments(@Param("current") Long current, @Param("size") Long size, @Param("commentVO") CommentVO commentVO);

    /**
     * get reply by comment id list
     *
     * @param commentIdList comment id list
     * @return reply list
     */
    List<ReplyDTO> listReplies(@Param("commentIdList") List<Integer> commentIdList);

    /**
     * get reply by comment id
     *
     * @param commentId comment id
     * @param current   page
     * @param size      size
     * @return reply list
     */
    List<ReplyDTO> listRepliesByCommentId(@Param("current") Long current, @Param("size") Long size, @Param("commentId") Integer commentId);

    /**
     * get reply num by comment id list
     *
     * @param commentIdList comment id list
     * @return reply num
     */
    List<ReplyCountDTO> listReplyCountByCommentId(@Param("commentIdList") List<Integer> commentIdList);

    /**
     * get comment num by topic ids
     *
     * @param topicIdList topic id list
     * @param type comment type
     * @return {@link List<CommentCountDTO>} comment num
     */
    List<CommentCountDTO> listCommentCountByTopicIds(List<Integer> topicIdList, Integer type);

    /**
     * get backend comment
     *
     * @param current   page
     * @param size      size
     * @param condition condition
     * @return comments
     */
    List<CommentBackDTO> listArticleCommentBackDTO(@Param("current") Long current, @Param("size") Long size,
                                            @Param("condition") ConditionVO condition);

    /**
     * get backend comments
     *
     * @param current   page
     * @param size      size
     * @param condition condition
     * @return comments
     */
    List<CommentBackDTO> listTalkCommentBackDTO(@Param("current") Long current, @Param("size") Long size,
                                                   @Param("condition") ConditionVO condition);

    /**
     * get comment statistics
     *
     * @param condition condition
     * @return comment num
     */
    Integer countCommentDTO(@Param("condition") ConditionVO condition);
}
