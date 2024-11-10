package com.example.training.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Xuxinyuan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "文档合集")
public class FileCollectionVO {
    /**
     * 文档合集id
     */
    @ApiModelProperty(name = "id", value = "文档合集id", required = true, dataType = "Integer")
    private Integer id;

    /**
     * 文档合集名
     */
    @NotBlank(message = "文档合集名不能为空")
    @ApiModelProperty(name = "collectionName", value = "文档合集名", required = true, dataType = "String")
    private String collectionName;

    /**
     * 文档合集描述
     */
    @ApiModelProperty(name = "collectionDesc", value = "文档合集描述", dataType = "String")
    private String collectionDesc;

    /**
     * 文档合集封面
     */
    @NotBlank(message = "文档合集封面不能为空")
    @ApiModelProperty(name = "albumCover", value = "文档合集封面", required = true, dataType = "String")
    private String collectionCover;

    /**
     * 状态值 1公开 2私密
     */
    @ApiModelProperty(name = "status", value = "状态值", required = true, dataType = "Integer")
    private Integer status;

    /**
     * 文件类型 1文档 2视频
     */
    @ApiModelProperty(name = "type", value = "文件类型", required = true, dataType = "Integer")
    private Integer type;

    /**
     * 练习合集id
     */
    @ApiModelProperty(name = "projectIds", value = "练习合集id", dataType = "List<Integer>")
    private List<Integer> projectIds;
}
