package com.example.training.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Xinyuan Xu
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "upload.hdfs")
public class HDFSConfigProperties {
    private String url;

    private String path;
}
