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
public class NoticeBackDTO {
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
     * sent time
     */
    private LocalDateTime createTime;

    /**
     * isDelete
     */
    private Boolean isDelete;

    /**
     * isTop
     */
    private Integer isTop;
}
