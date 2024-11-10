package com.example.training.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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
@ApiModel(description = "后台订单")
public class OrderBackVO {
    /**
     * id
     */
    @NotNull(message = "id不能为空")
    @ApiModelProperty(name = "id", value = "id", required = true, dataType = "Integer")
    private Integer id;

    /**
     * 地址id
     */
    @ApiModelProperty(name = "id", value = "地址id", dataType = "Integer")
    private Integer addressId;

    /**
     * 物流
     */
    @ApiModelProperty(name = "freights", value = "物流", dataType = "List<Map<String, Object>>")
    private List<Map<String, Object>> freights;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern= "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "endTime", value = "结束时间", dataType = "LocalDateTime")
    private LocalDateTime endTime;
}
