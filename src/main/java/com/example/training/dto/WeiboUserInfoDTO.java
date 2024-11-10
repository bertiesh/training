package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xinyuan Xu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeiboUserInfoDTO {
    /**
     * 昵称
     */
    private String screen_name;

    /**
     * 头像
     */
    private String avatar_hd;
}
