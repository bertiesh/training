package com.example.training.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xinyuan Xy
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SKUBackDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 款式规格
     */
    private String name;

    /**
     * 款式图
     */
    private String picture;

    /**
     * 重量
     */
    private Double weight;

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
     * 销售量
     */
    private Integer salesCount;

    /**
     * 是否删除
     */
    private Boolean isDelete;
}
