package com.example.training.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Xuxinyuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_sku")
public class SKU {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * spu id
     */
    private Integer spuId;

    /**
     * 款式规格
     */
    private String name;

    /**
     * 款式图
     */
    private String picture;

    /**
     * 存货量
     */
    private Integer stock;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 积分价格
     */
    private Integer points;

    /**
     * 市场价
     */
    private Double price;

    /**
     * 成本价
     */
    private Double cost;

    /**
     * 重量
     */
    private Double weight;

    /**
     * 是否删除
     */
    private Boolean isDelete;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
