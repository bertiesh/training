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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "spu")
public class SPUVO {
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "id", dataType = "Integer")
    private Integer id;

    /**
     * 商品名
     */
    @ApiModelProperty(name = "name", value = "商品名", dataType = "String")
    private String name;

    /**
     * 详情页
     */
    @ApiModelProperty(name = "description", value = "详情页", dataType = "String")
    private String description;

    /**
     * 主图
     */
    @ApiModelProperty(name = "pictures", value = "主图", dataType = "String")
    private String pictures;

    /**
     * 是否上架
     */
    @ApiModelProperty(name = "isOn", value = "是否上架", dataType = "Boolean")
    private Boolean isOn;

    /**
     * 是否置顶
     */
    @ApiModelProperty(name = "isTop", value = "是否置顶", dataType = "Integer")
    private Integer isTop;

    /**
     * 是否删除
     */
    @ApiModelProperty(name = "isDelete", value = "是否删除", dataType = "Boolean")
    private Boolean isDelete;
}
