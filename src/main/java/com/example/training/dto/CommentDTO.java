package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    /**
     * comment id
     */
    private Integer id;

    /**
     * user id
     */
    private Integer userId;

    /**
     * nickname
     */
    private String nickname;

    /**
     * avatar
     */
    private String avatar;

    /**
     * article id
     */
    private Integer topicId;

    /**
     * commentContent
     */
    private String commentContent;

    /**
     * likeCount
     */
    private Integer likeCount;

    /**
     * isLike
     */
    private Boolean isLike;

    /**
     * comment time
     */
    private LocalDateTime createTime;

    /**
     * replyCount
     */
    private Integer replyCount;

    /**
     * replies
     */
    private List<ReplyDTO> replyDTOList;
}
