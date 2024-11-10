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
@ApiModel(description = "回答")
public class PostVO {
    /**
     * 回答id
     */
    @ApiModelProperty(name = "id", value = "回答id", dataType = "Integer")
    private Integer id;

    /**
     * 项目id
     */
    @ApiModelProperty(name = "projectId", value = "项目id", dataType = "Integer")
    private Integer projectId;

    /**
     * 回答
     */
    @ApiModelProperty(name = "answer", value = "回答", dataType = "String")
    private String answer;

    /**
     * 是否删除
     */
    @ApiModelProperty(name = "isDelete", value = "是否删除", dataType = "Boolean")
    private Boolean isDelete;
}
