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
@ApiModel(description = "购物车")
public class ShoppingCartVO {
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "id", dataType = "Integer")
    private Integer id;

    /**
     * sku id
     */
    @ApiModelProperty(name = "spuId", value = "sku id", required = true, dataType = "Integer")
    private Integer skuId;

    /**
     * 数量
     */
    @ApiModelProperty(name = "num", value = "数量", required = true, dataType = "Integer")
    private Integer num;

    /**
     * 添加数量
     */
    @ApiModelProperty(name = "addNum", value = "添加数量", required = true, dataType = "Integer")
    private Integer addNum;

    /**
     * 是否删除
     */
    @ApiModelProperty(name = "isDelete", value = "是否删除", dataType = "Integer")
    private Integer isDelete;
}
