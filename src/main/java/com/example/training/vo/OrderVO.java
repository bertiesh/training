package com.example.training.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Xuxinyuan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "订单")
public class OrderVO {
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "id", dataType = "Integer")
    private Integer id;

    /**
     * 地址id
     */
    @ApiModelProperty(name = "id", value = "地址id", dataType = "Integer")
    private Integer addressId;

    /**
     * 订单状态
     */
    @ApiModelProperty(name = "status", value = "订单状态", dataType = "Integer")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remarks", value = "备注", dataType = "String")
    private String remarks;

    /**
     * 结束时间
     */
    @ApiModelProperty(name = "endTime", value = "结束时间", dataType = "LocalDateTime")
    private LocalDateTime endTime;

    /**
     * 订单内容
     */
    @ApiModelProperty(name = "orderItems", value = "订单内容", dataType = "Map<Integer, Integer>")
    private Map<Integer, Integer> orderItems;

    /**
     * 购物车id
     */
    @ApiModelProperty(name = "shoppingCartIds", value = "购物车id", dataType = "List<Integer>")
    private List<Integer> shoppingCartIds;
}
