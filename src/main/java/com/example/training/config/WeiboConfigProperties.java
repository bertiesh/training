package com.example.training.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Xinyuan Xu
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "weibo")
public class WeiboConfigProperties {
    /**
     * appId
     */
    private String appId;

    /**
     * appSecret
     */
    private String appSecret;

    /**
     * login type
     */
    private String grantType;

    /**
     * redirect url
     */
    private String redirectUrl;

    /**
     * access token address
     */
    private String accessTokenUrl;

    /**
     * user info url
     */
    private String userInfoUrl;
}
