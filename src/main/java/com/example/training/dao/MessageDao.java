package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface MessageDao extends BaseMapper<Message> {
    /**
     * query the current user's conversation list, return the latest message for each conversation.
     * @param userId user id
     * @return List<Message>
     */
    List<Message> selectConversations(Integer userId);

    /**
     * query the list of messages contained in a conversation
     *
     * @param conversationCode conversation code
     * @param start            start
     * @param size             size
     * @param userInfoId
     * @return List<Message>
     */
    List<Message> selectMessages(String conversationCode, int start, int size, Integer userInfoId);
}
