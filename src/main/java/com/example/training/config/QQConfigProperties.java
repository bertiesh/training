package com.example.training.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Xinyuan Xu
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "qq")
public class QQConfigProperties {
    /**
     * QQ appId
     */
    private String appId;

    /**
     * token address
     */
    private String checkTokenUrl;

    /**
     * QQ user id address
     */
    private String userInfoUrl;
}
