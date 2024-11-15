package com.example.training.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Xuxinyuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "查询条件")
public class ConditionVO {
    /**
     * 页码
     */
    @ApiModelProperty(name = "current", value = "页码", dataType = "Long")
    private Long current;

    /**
     * 条数
     */
    @ApiModelProperty(name = "size", value = "条数", dataType = "Long")
    private Long size;

    /**
     * 搜索内容
     */
    @ApiModelProperty(name = "keywords", value = "搜索内容", dataType = "String")
    private String keywords;

    /**
     * 分类id
     */
    @ApiModelProperty(name = "categoryId", value = "分类id", dataType = "Integer")
    private Integer categoryId;

    /**
     * 标签id
     */
    @ApiModelProperty(name = "tagId", value = "标签id", dataType = "Integer")
    private Integer tagId;

    /**
     * 用户信息id
     */
    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Integer")
    private Integer userInfoId;

    /**
     * 相册id
     */
    @ApiModelProperty(name = "albumId", value = "相册id", dataType = "Integer")
    private Integer albumId;

    /**
     * 帮助id
     */
    @ApiModelProperty(name = "helpId", value = "帮助id", dataType = "Integer")
    private Integer helpId;

    /**
     * 物流id
     */
    @ApiModelProperty(name = "freightId", value = "物流id", dataType = "Integer")
    private Integer freightId;

    /**
     * 订单id
     */
    @ApiModelProperty(name = "orderId", value = "订单id", dataType = "Integer")
    private Integer orderId;

    /**
     * spu id
     */
    @ApiModelProperty(name = "spuId", value = "spu id", dataType = "Integer")
    private Integer spuId;

    /**
     * 登录类型
     */
    @ApiModelProperty(name = "type", value = "登录类型", dataType = "Integer")
    private Integer loginType;

    /**
     * 类型
     */
    @ApiModelProperty(name = "type", value = "类型", dataType = "Integer")
    private Integer type;

    /**
     * 状态
     */
    @ApiModelProperty(name = "status", value = "状态", dataType = "Integer")
    private Integer status;

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

    /**
     * 是否删除
     */
    @ApiModelProperty(name = "isDelete", value = "是否删除", dataType = "Integer")
    private Integer isDelete;

    /**
     * 是否审核
     */
    @ApiModelProperty(name = "isReview", value = "是否审核", dataType = "Integer")
    private Integer isReview;

    /**
     * 是否上架
     */
    @ApiModelProperty(name = "isOn", value = "是否上架", dataType = "Integer")
    private Integer isOn;
}
