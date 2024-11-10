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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户文档权限")
public class UserCollectionVO {
    /**
     * 用户文档
     */
    @NotNull(message = "用户文档不能为空")
    @ApiModelProperty(name = "collectionIdList", value = "文档id集合", dataType = "List<Integer>")
    private List<Integer> collectionIdList;
}
