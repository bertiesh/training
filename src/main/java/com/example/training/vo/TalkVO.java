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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "讨论对象")
public class TalkVO {
    /**
     * 讨论id
     */
    @ApiModelProperty(name = "id", value = "讨论id", dataType = "Integer")
    private Integer id;

    /**
     * 讨论内容
     */
    @ApiModelProperty(name = "content", value = "讨论内容", dataType = "String")
    private String content;

    /**
     * 图片
     */
    @ApiModelProperty(name = "images", value = "讨论图片", dataType = "String")
    private String images;

    /**
     * 是否置顶
     */
    @ApiModelProperty(name = "isTop", value = "置顶状态", dataType = "Integer")
    private Integer isTop;

    /**
     * 讨论状态 1.公开 2.私密
     */
    @ApiModelProperty(name = "status", value = "讨论状态", dataType = "Integer")
    private Integer status;
}
