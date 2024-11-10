package com.example.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.training.dto.MessageDTO;
import com.example.training.dto.MessageInfoDTO;
import com.example.training.dto.NoticeBackDTO;
import com.example.training.dto.NoticeDTO;
import com.example.training.entity.Message;
import com.example.training.vo.*;

import java.util.List;
import java.util.Map;

/**
 * @author Xuxinyuan
 */
public interface MessageService extends IService<Message> {
    /**
     * 发送私信
     *
     * @param messageVO 私信信息
     */
    void saveMessage(MessageVO messageVO);

    /**
     * 查看对话
     *
     * @return List< MessageDTO >} 私信列表
     */
    List<MessageDTO> listMessages();

    /**
     * 查看对话信息
     *
     * @param conversationVO 对话信息
     * @return {@link MessageInfoDTO} 私信信息列表
     */
    MessageInfoDTO listMessagesInfo(ConversationVO conversationVO);

    /**
     * 更新对话信息
     * @param conversationVO 对话信息
     */
    void updateConversations(ConversationVO conversationVO);

    /**
     * 查看通知
     * @return {@link Result<java.util.Map>} 通知列表
     */
    Map<String, Integer> listNotices();

    /**
     * 查看通知详情
     *
     * @param type    通知类型
     * @param current
     * @param size
     * @return {@link Result<NoticeDTO>} 通知详情列表
     */
    PageResult<NoticeDTO> listNoticesInfo(String type, Integer current, Integer size);

    /**
     * 发送系统通知
     *
     * @param messageVO 通知信息
     */
    void saveAdminMessage(MessageVO messageVO);

    /**
     * 查看通知详情
     *
     * @return {@link Result<NoticeBackDTO>} 通知详情列表
     */
    PageResult<NoticeBackDTO> listNoticesBackInfo(ConditionVO condition);

    /**
     * 更新通知详情
     *
     * @param messageVO 通知信息
     */
    void updateNoticesBackInfo(MessageVO messageVO);

    /**
     * 群发邮件通知
     *
     * @param message 通知信息
     * @param subject 邮件标题
     */
    void sendAdminMessage(String message, String subject);
}
