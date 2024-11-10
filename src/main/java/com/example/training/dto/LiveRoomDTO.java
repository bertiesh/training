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
public class LiveRoomDTO {
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
     * live type
     */
    private Integer type;

    /**
     * screenType
     */
    private Integer screenType;

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
     * isLive
     */
    private Integer isLive;

    /**
     * liveUrl
     */
    private String liveUrl;

    /**
     * startTime
     */
    private LocalDateTime startTime;

    /**
     * endTime
     */
    private LocalDateTime endTime;
}
