package com.example.training.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 留言
 * @author Xuxinyuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_message")
public class Message {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发送者
     */
    private Integer fromId;

    /**
     * 发送对象
     */
    private Integer toId;

    /**
     * 用户ip
     */
    private String ipAddress;

    /**
     * 用户地址
     */
    private String ipSource;

    /**
     * 私信内容
     */
    private String messageContent;

    /**
     * 对话编码
     */
    private String conversationCode;

    /**
     * 是否已读
     */
    private Boolean status;

    /**
     * 是否删除
     */
    private Boolean isDelete;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 关注评论点赞人id
     */
    private Integer fromUserId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 被点赞评论的内容的id
     */
    private Integer commentedId;

    /**
     * 被点赞评论的内容
     */
    private String commentedContent;

    /**
     * 原文作者昵称
     */
    private String originalUserName;

    /**
     * 原文id
     */
    private Integer originalId;

    /**
     * 原文内容
     */
    private String originalContent;

    /**
     * 原文类型
     */
    private Integer originalType;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
