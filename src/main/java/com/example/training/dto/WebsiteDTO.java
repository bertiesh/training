package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebsiteDTO {
    /**
     * 网站头像
     */
    private String websiteAvatar;

    /**
     * 网站名称
     */
    private String websiteName;

    /**
     * 网站介绍
     */
    private String websiteIntro;

    /**
     * 网站创建时间
     */
    private String websiteCreateTime;

    /**
     * 网站备案号
     */
    private String websiteRecordNo;

    /**
     * 默认问卷
     */
    private List<Integer> defaultSurvey;
}
