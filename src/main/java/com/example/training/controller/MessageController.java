package com.example.training.controller;

import com.example.training.annotation.AccessLimit;
import com.example.training.dto.MessageDTO;
import com.example.training.dto.MessageInfoDTO;
import com.example.training.dto.NoticeBackDTO;
import com.example.training.dto.NoticeDTO;
import com.example.training.service.MessageService;
import com.example.training.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author Xinyuan Xu
 */
@Api(tags = "私信模块")
@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;

    /**
     * send message
     *
     * @param messageVO message info
     * @return {@link Result <>}
     */
    @AccessLimit(seconds = 60, maxCount = 1)
    @ApiOperation(value = "发送私信")
    @PostMapping("/messages")
    public Result<?> saveMessage(@Valid @RequestBody MessageVO messageVO) {
        messageService.saveMessage(messageVO);
        return Result.ok();
    }

    /**
     * get chats
     *
     * @return {@link Result< MessageDTO >} message list
     */
    @ApiOperation(value = "查看私信列表")
    @GetMapping("/messages")
    public Result<List<MessageDTO>> listMessages() {
        return Result.ok(messageService.listMessages());
    }

    /**
     * get message
     *
     * @param conversationVO conversation info
     * @return {@link Result<MessageInfoDTO>} conversation info list
     */
    @ApiOperation(value = "查看私信信息列表")
    @PostMapping("/messages/info")
    public Result<MessageInfoDTO> listMessagesInfo(@Valid @RequestBody ConversationVO conversationVO) {
        return Result.ok(messageService.listMessagesInfo(conversationVO));
    }

    /**
     * update conversation info
     *
     * @param conversationVO conversation info
     */
    @ApiOperation(value = "更新对话")
    @PostMapping("/conversations")
    public Result<?> updateConversations(@Valid @RequestBody ConversationVO conversationVO) {
        messageService.updateConversations(conversationVO);
        return Result.ok();
    }

    /**
     * search notification
     *
     * @return {@link Result<Map>} notification list
     */
    @ApiOperation(value = "查看通知列表")
    @GetMapping("/notices")
    public Result<Map<String, Integer>> listNotices() {
        return Result.ok(messageService.listNotices());
    }

    /**
     * get notification details
     *
     * @param type notification type
     * @return {@link Result<NoticeDTO>} notification details list
     */
    @ApiOperation(value = "查看通知详情列表")
    @GetMapping("/notices/{type}")
    public Result<PageResult<NoticeDTO>> listNoticesInfo(@PathVariable("type") String type, @RequestParam Integer size,
                                                         @RequestParam Integer current) {
        return Result.ok(messageService.listNoticesInfo(type, current, size));
    }

    /**
     * send notification
     *
     * @param messageVO notification info
     * @return {@link Result <>}
     */
    @AccessLimit(seconds = 60, maxCount = 1)
    @ApiOperation(value = "发送通知")
    @PostMapping("/admin/messages")
    public Result<?> saveAdminMessage(@Valid @RequestBody MessageVO messageVO) {
        messageService.saveAdminMessage(messageVO);
        return Result.ok();
    }

    /**
     * get notification details
     *
     * @return {@link Result<NoticeBackDTO>} notification details list
     */
    @ApiOperation(value = "查看通知详情列表")
    @GetMapping("/admin/notices")
    public Result<PageResult<NoticeBackDTO>> listNoticesBackInfo(ConditionVO condition) {
        return Result.ok(messageService.listNoticesBackInfo(condition));
    }

    /**
     * update notification details
     *
     * @param messageVO notification info
     * @return {@link Result<>}
     */
    @ApiOperation(value = "查看通知详情列表")
    @PostMapping("/admin/notices")
    public Result<?> updateNoticesBackInfo(@Valid @RequestBody MessageVO messageVO) {
        messageService.updateNoticesBackInfo(messageVO);
        return Result.ok();
    }

    /**
     * send group email
     *
     * @param message email content
     * @param subject email title
     * @return {@link Result <>}
     */
    @AccessLimit(seconds = 60, maxCount = 1)
    @ApiOperation(value = "群发邮件通知")
    @GetMapping("/admin/messages/email")
    public Result<?> sendAdminMessage(String message, String subject) {
        messageService.sendAdminMessage(message, subject);
        return Result.ok();
    }
}
