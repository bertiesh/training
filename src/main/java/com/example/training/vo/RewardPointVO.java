package com.example.training.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Xuxinyuan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "积分")
public class RewardPointVO {
    /**
     * 用户id
     */
    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Integer")
    private Integer userId;

    /**
     * 事件
     */
    @ApiModelProperty(name = "event", value = "事件", dataType = "String")
    private String event;

    /**
     * 积分
     */
    @ApiModelProperty(name = "points", value = "积分", dataType = "Integer")
    private Integer points;
}
