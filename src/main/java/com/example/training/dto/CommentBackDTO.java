package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Backstage comments
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentBackDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * avatar
     */
    private String avatar;

    /**
     * nickname
     */
    private String nickname;

    /**
     * replied user nickname
     */
    private String replyNickname;

    /**
     * articleTitle
     */
    private String articleTitle;

    /**
     * content
     */
    private String content;

    /**
     * commentContent
     */
    private String commentContent;

    /**
     * comment type
     */
    private Integer type;

    /**
     * isReview
     */
    private Integer isReview;

    /**
     * createTime
     */
    private LocalDateTime createTime;
}
