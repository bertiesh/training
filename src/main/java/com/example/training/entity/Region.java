package com.example.training.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("tb_region")
public class Region {
    /**
     * 区划编号
     */
    @TableId(type = IdType.AUTO)
    private String code;

    /**
     * 父区划编号
     */
    private String parentCode;

    /**
     * 祖区划编号
     */
    private String ancestors;

    /**
     * 区划名称
     */
    private String name;

    /**
     * 省级区划编号
     */
    private String provinceCode;

    /**
     * 省级名称
     */
    private String provinceName;

    /**
     * 市级区划编号
     */
    private String cityCode;

    /**
     * 市级名称
     */
    private String cityName;

    /**
     * 区级区划编号
     */
    private String districtCode;

    /**
     * 区级名称
     */
    private String districtName;

    /**
     * 镇级区划编号
     */
    private String townCode;

    /**
     * 镇级名称
     */
    private String townName;

    /**
     * 村级区划编号
     */
    private String villageCode;

    /**
     * 村级名称
     */
    private String villageName;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;
}
