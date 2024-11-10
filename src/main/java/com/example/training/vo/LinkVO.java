package com.example.training.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Xuxinyuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "链接")
public class LinkVO {
    /**
     * id
     */
    @ApiModelProperty(name = "categoryId", value = "id", dataType = "Integer")
    private Integer id;

    /**
     * 链接名
     */
    @NotBlank(message = "链接名不能为空")
    @ApiModelProperty(name = "linkName", value = "链接名", dataType = "String", required = true)
    private String linkName;

    /**
     * 链接图片
     */
    @NotBlank(message = "链接图片不能为空")
    @ApiModelProperty(name = "linkPhoto", value = "链接图片", dataType = "String", required = true)
    private String linkPhoto;

    /**
     * 链接地址
     */
    @NotBlank(message = "链接地址不能为空")
    @ApiModelProperty(name = "linkAddress", value = "链接地址", dataType = "String", required = true)
    private String linkAddress;

    /**
     * 介绍
     */
    @NotBlank(message = "链接介绍不能为空")
    @ApiModelProperty(name = "linkIntro", value = "介绍", dataType = "String", required = true)
    private String linkIntro;
}
