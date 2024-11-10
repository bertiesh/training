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
@ApiModel(description = "sku")
public class SKUVO {
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "id", dataType = "Integer")
    private Integer id;

    /**
     * spu id
     */
    @ApiModelProperty(name = "spuId", value = "spu id", dataType = "Integer")
    private Integer spuId;

    /**
     * 款式规格
     */
    @ApiModelProperty(name = "name", value = "款式规格", dataType = "String")
    private String name;

    /**
     * 款式图
     */
    @ApiModelProperty(name = "picture", value = "款式图", dataType = "String")
    private String picture;

    /**
     * 存货量
     */
    @ApiModelProperty(name = "stock", value = "存货量", dataType = "Integer")
    private Integer stock;

    /**
     * 排序
     */
    @ApiModelProperty(name = "sort", value = "排序", dataType = "Integer")
    private Integer sort;

    /**
     * 积分价格
     */
    @ApiModelProperty(name = "points", value = "积分价格", dataType = "Integer")
    private Integer points;

    /**
     * 市场价
     */
    @ApiModelProperty(name = "price", value = "市场价", dataType = "Double")
    private Double price;

    /**
     * 成本价
     */
    @ApiModelProperty(name = "cost", value = "成本价", dataType = "Double")
    private Double cost;

    /**
     * 是否删除
     */
    @ApiModelProperty(name = "isDelete", value = "是否删除", dataType = "Boolean")
    private Boolean isDelete;
}
