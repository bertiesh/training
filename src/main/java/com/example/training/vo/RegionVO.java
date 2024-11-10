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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "行政区划")
public class RegionVO {
    /**
     * 区划编号
     */
    @ApiModelProperty(name = "code", value = "区划编号", dataType = "String")
    private String code;

    /**
     * 父区划编号
     */
    @ApiModelProperty(name = "parentCode", value = "父区划编号", dataType = "String")
    private String parentCode;

    /**
     * 祖区划编号
     */
    @ApiModelProperty(name = "ancestors", value = "祖区划编号", dataType = "String")
    private String ancestors;

    /**
     * 区划名称
     */
    @ApiModelProperty(name = "name", value = "区划名称", dataType = "String")
    private String name;

    /**
     * 省级区划编号
     */
    @ApiModelProperty(name = "provinceCode", value = "省级区划编号", dataType = "String")
    private String provinceCode;

    /**
     * 省级名称
     */
    @ApiModelProperty(name = "provinceName", value = "省级名称", dataType = "String")
    private String provinceName;

    /**
     * 市级区划编号
     */
    @ApiModelProperty(name = "cityCode", value = "市级区划编号", dataType = "String")
    private String cityCode;

    /**
     * 市级名称
     */
    @ApiModelProperty(name = "cityName", value = "市级名称", dataType = "String")
    private String cityName;

    /**
     * 区级区划编号
     */
    @ApiModelProperty(name = "districtCode", value = "区级区划编号", dataType = "String")
    private String districtCode;

    /**
     * 区级名称
     */
    @ApiModelProperty(name = "districtName", value = "区级名称", dataType = "String")
    private String districtName;

    /**
     * 镇级区划编号
     */
    @ApiModelProperty(name = "townCode", value = "镇级区划编号", dataType = "String")
    private String townCode;

    /**
     * 镇级名称
     */
    @ApiModelProperty(name = "townName", value = "镇级名称", dataType = "String")
    private String townName;

    /**
     * 村级区划编号
     */
    @ApiModelProperty(name = "villageCode", value = "村级区划编号", dataType = "String")
    private String villageCode;

    /**
     * 村级名称
     */
    @ApiModelProperty(name = "villageName", value = "村级名称", dataType = "String")
    private String villageName;

    /**
     * 层级
     */
    @ApiModelProperty(name = "level", value = "层级", dataType = "Integer")
    private Integer level;

    /**
     * 排序
     */
    @ApiModelProperty(name = "sort", value = "排序", dataType = "Integer")
    private Integer sort;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注", dataType = "String")
    private String remark;
}
