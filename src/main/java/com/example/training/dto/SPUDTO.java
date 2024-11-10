package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SPUDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * spu名称
     */
    private String name;

    /**
     * 详情页
     */
    private String description;

    /**
     * 主图
     */
    private String pictures;

    /**
     * 主图列表
     */
    private List<String> pictureList;

    /**
     * 购买量
     */
    private Integer salesCount;

    /**
     * sku
     */
    private List<SKUDTO> skuDTOList;

    /**
     * 最低价格
     */
    private Integer minPoints;
}
