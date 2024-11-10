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
public class QQTokenDTO {
    /**
     * openid
     */
    private String openid;

    /**
     * 客户端id
     */
    private String client_id;
}
