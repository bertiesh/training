package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 操作日志
 * @author Xuxinyuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationLogDTO {
    /**
     * log id
     */
    private Integer id;

    /**
     * operation module
     */
    private String optModule;

    /**
     * operation url
     */
    private String optUrl;

    /**
     * operation type
     */
    private String optType;

    /**
     * operation method
     */
    private String optMethod;

    /**
     * operation description
     */
    private String optDesc;

    /**
     * request method
     */
    private String requestMethod;

    /**
     * requestParam
     */
    private String requestParam;

    /**
     * responseData
     */
    private String responseData;

    /**
     * nickname
     */
    private String nickname;

    /**
     * ipAddress
     */
    private String ipAddress;

    /**
     * ipSource
     */
    private String ipSource;

    /**
     * createTime
     */
    private LocalDateTime createTime;
}
