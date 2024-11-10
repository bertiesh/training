package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SKUDTO {
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
}
