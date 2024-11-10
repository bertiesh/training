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
public class RewardPointDTO {
    /**
     * 事件
     */
    private String event;

    /**
     * 积分变化
     */
    private Integer points;

    /**
     * 现有积分
     */
    private Integer totalPoints;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
