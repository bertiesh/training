package com.example.training.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xuxinyuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "对话")
public class ConversationVO {
    /**
     * 对话编码
     */
    @ApiModelProperty(name = "conversationCode", value = "对话编码", dataType = "String")
    private String conversationCode;

    /**
     * 页码
     */
    @ApiModelProperty(name = "current", value = "页码", dataType = "Integer")
    private Integer current;

    /**
     * 条数
     */
    @ApiModelProperty(name = "size", value = "条数", dataType = "Integer")
    private Integer size;

    /**
     * 是否删除
     */
    @ApiModelProperty(name = "isDelete", value = "是否删除", dataType = "Boolean")
    private Boolean isDelete;

    /**
     * 是否置顶
     */
    @ApiModelProperty(name = "isTop", value = "是否置顶", dataType = "Integer")
    private Integer isTop;
}
