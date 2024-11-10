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
public class SPUBackDTO {
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
     * 浏览量
     */
    private Integer viewsCount;

    /**
     * 购买量
     */
    private Integer salesCount;

    /**
     * 是否上架
     */
    private Boolean isOn;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 是否删除
     */
    private Boolean isDelete;

    /**
     * sku
     */
    private List<SKUBackDTO> skuBackDTOList;
}
