package com.example.training.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Xuxinyuan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "网站配置")
public class WebsiteConfigVO {
    /**
     * 网站头像
     */
    @ApiModelProperty(name = "websiteAvatar", value = "网站头像", required = true, dataType = "String")
    private String websiteAvatar;

    /**
     * 网站名称
     */
    @ApiModelProperty(name = "websiteName", value = "网站名称", required = true, dataType = "String")
    private String websiteName;

    /**
     * 网站作者
     */
    @ApiModelProperty(name = "websiteAuthor", value = "网站作者", required = true, dataType = "String")
    private String websiteAuthor;

    /**
     * 网站介绍
     */
    @ApiModelProperty(name = "websiteIntro", value = "网站介绍", required = true, dataType = "String")
    private String websiteIntro;

    /**
     * 网站注意事项
     */
    @ApiModelProperty(name = "websiteNotice", value = "网站注意事项", required = true, dataType = "String")
    private String websiteNotice;

    /**
     * 网站创建时间
     */
    @ApiModelProperty(name = "websiteCreateTime", value = "网站创建时间", required = true, dataType = "LocalDateTime")
    private String websiteCreateTime;

    /**
     * 网站备案号
     */
    @ApiModelProperty(name = "websiteRecordNo", value = "网站备案号", required = true, dataType = "String")
    private String websiteRecordNo;

    /**
     * 社交url列表
     */
    @ApiModelProperty(name = "socialUrlList", value = "社交url列表", required = true, dataType = "List<String>")
    private List<String> socialUrlList;

    /**
     * github
     */
    @ApiModelProperty(name = "github", value = "github", required = true, dataType = "String")
    private String github;

    /**
     * gitee
     */
    @ApiModelProperty(name = "gitee", value = "gitee", required = true, dataType = "String")
    private String gitee;

    /**
     * 用户头像
     */
    @ApiModelProperty(name = "userAvatar", value = "用户头像", required = true, dataType = "String")
    private String userAvatar;

    /**
     * 是否评论审核
     */
    @ApiModelProperty(name = "isCommentReview", value = "是否评论审核", required = true, dataType = "Integer")
    private Integer isCommentReview;

    /**
     * 是否邮箱通知
     */
    @ApiModelProperty(name = "isEmailNotice", value = "是否邮箱通知", required = true, dataType = "Integer")
    private Integer isEmailNotice;

    /**
     * 文章封面
     */
    @ApiModelProperty(name = "articleCover", value = "文章封面", required = true, dataType = "String")
    private String articleCover;

    /**
     * 是否开启聊天室
     */
    @ApiModelProperty(name = "isReward", value = "是否开启聊天室", required = true, dataType = "Integer")
    private Integer isChatRoom;

    /**
     * websocket地址
     */
    @ApiModelProperty(name = "websocketUrl", value = "websocket地址", required = true, dataType = "String")
    private String websocketUrl;

    /**
     * 是否开启音乐
     */
    @ApiModelProperty(name = "isMusicPlayer", value = "是否开启音乐", required = true, dataType = "Integer")
    private Integer isMusicPlayer;

    /**
     * 默认密码
     */
    @ApiModelProperty(name = "defaultPassword", value = "默认密码", required = true, dataType = "String")
    private String defaultPassword;

    /**
     * 默认问卷
     */
    @ApiModelProperty(name = "defaultSurvey", value = "默认问卷", required = true, dataType = "List<Integer>")
    private List<Integer> defaultSurvey;
}
