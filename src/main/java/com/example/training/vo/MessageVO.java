package com.example.training.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Xuxinyuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "私信")
public class MessageVO {
    /**
     * 私信id
     */
    @ApiModelProperty(name = "id", value = "私信id", dataType = "Integer")
    private Integer id;

    /**
     * 接收者id
     */
    @ApiModelProperty(name = "toId", value = "被私信id", required = true, dataType = "Integer")
    private Integer toId;

    /**
     * 私信内容
     */
    @ApiModelProperty(name = "messageContent", value = "私信内容", required = true, dataType = "String")
    private String messageContent;

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
