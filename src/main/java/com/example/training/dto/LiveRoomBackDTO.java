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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LiveRoomBackDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * live room name
     */
    private String name;

    /**
     * live room description
     */
    private String description;

    /**
     * host id
     */
    private Integer userInfoId;

    /**
     * host
     */
    private UserInfoDTO userInfoDTO;

    /**
     * live room cover
     */
    private String cover;

    /**
     * whether to allow comments
     */
    private Integer displayComment;

    /**
     * whether to pin it to the top
     */
    private Integer isTop;

    /**
     * isLive
     */
    private Integer isLive;

    /**
     * isDelete
     */
    private Integer isDelete;

    /**
     * live url
     */
    private String liveUrl;

    /**
     * live type
     */
    private Integer type;

    /**
     * screenType
     */
    private Integer screenType;

    /**
     * startTime
     */
    private LocalDateTime startTime;

    /**
     * endTime
     */
    private LocalDateTime endTime;
}
