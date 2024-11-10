package com.example.training.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Xuxinyuan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "直播间")
public class LiveRoomVO {
    /**
     * 直播间id
     */
    @ApiModelProperty(name = "id", value = "文档合集id", dataType = "Integer")
    private Integer id;

    /**
     * 主播id
     */
    @ApiModelProperty(name = "userInfoId", value = "主播id", required = true, dataType = "Integer")
    private Integer userInfoId;

    /**
     * 直播间名
     */
    @NotBlank(message = "直播间名不能为空")
    @ApiModelProperty(name = "name", value = "直播间名", required = true, dataType = "String")
    private String name;

    /**
     * 直播间描述
     */
    @ApiModelProperty(name = "description", value = "直播间描述", dataType = "String")
    private String description;

    /**
     * 直播url
     */
    @ApiModelProperty(name = "liveUrl", value = "直播url", dataType = "String")
    private String liveUrl;

    /**
     * 直播间封面
     */
    @NotBlank(message = "直播间封面不能为空")
    @ApiModelProperty(name = "cover", value = "直播间封面", required = true, dataType = "String")
    private String cover;

    /**
     * 屏幕类型
     */
    @ApiModelProperty(name = "screenType", value = "屏幕类型", required = true, dataType = "Integer")
    private Integer screenType;

    /**
     * 直播类型
     */
    @ApiModelProperty(name = "type", value = "直播类型", required = true, dataType = "Integer")
    private Integer type;

    /**
     * 是否置顶
     */
    @ApiModelProperty(name = "isTop", value = "是否置顶", dataType = "Integer")
    private Integer isTop;

    /**
     * 是否删除
     */
    @ApiModelProperty(name = "isDelete", value = "是否删除", dataType = "Integer")
    private Integer isDelete;

    /**
     * 是否允许评论
     */
    @ApiModelProperty(name = "displayComment", value = "是否允许评论", dataType = "Integer")
    private Integer displayComment;

    /**
     * 开始时间
     */
    @ApiModelProperty(name = "startTime", value = "开始时间", dataType = "LocalDateTime")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(name = "endTime", value = "结束时间", dataType = "LocalDateTime")
    private LocalDateTime endTime;
}
