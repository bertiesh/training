package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * notice code
     */
    private String conversationCode;

    /**
     * notice content
     */
    private String messageContent;

    /**
     * follow/comment/like user id
     */
    private Integer fromUserId;

    /**
     * follow/comment/like username
     */
    private String fromUserName;

    /**
     * commentContent
     */
    private String commentContent;

    /**
     * commented/liked content id
     */
    private Integer commentedId;

    /**
     * commented/liked content
     */
    private String commentedContent;

    /**
     * original content author name
     */
    private String originalUserName;

    /**
     * original content id
     */
    private Integer originalId;

    /**
     * original content
     */
    private String originalContent;

    /**
     * original content type
     */
    private Integer originalType;

    /**
     * sent time
     */
    private LocalDateTime createTime;
}
