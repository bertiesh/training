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
@ApiModel(description = "文档信息")
public class FileInfoVO {
    /**
     * 文档id
     */
    @NotNull(message = "文档id不能为空")
    @ApiModelProperty(name = "id", value = "文档id", required = true, dataType = "Integer")
    private Integer id;

    /**
     * 文档名
     */
    @NotBlank(message = "文档名不能为空")
    @ApiModelProperty(name = "fileName", value = "文档名", required = true, dataType = "String")
    private String fileName;

    /**
     * 文档描述
     */
    @ApiModelProperty(name = "fileDesc", value = "文档描述", dataType = "String")
    private String fileDesc;

    /**
     * 文档类型
     */
    @NotNull(message = "文档类型不能为空")
    @ApiModelProperty(name = "type", value = "文档类型", required = true, dataType = "Integer")
    private Integer type;
}
