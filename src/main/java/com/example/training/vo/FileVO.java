package com.example.training.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Xuxinyuan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "文档")
public class FileVO {
    /**
     * 文档合集id
     */
    @NotNull(message = "文档合集id不能为空")
    @ApiModelProperty(name = "id", value = "文档合集id", required = true, dataType = "Integer")
    private Integer collectionId;

    /**
     * 文档url列表
     */
    @ApiModelProperty(name = "photoUrlList", value = "文档列表", required = true, dataType = "List<String>")
    private List<String> fileUrlList;

    /**
     * 文档id列表
     */
    @ApiModelProperty(name = "fileIdList", value = "文档id列表", required = true, dataType = "List<Integer>")
    private List<Integer> fileIdList;
}
