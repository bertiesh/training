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
public class ShoppingCartDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * spu id
     */
    private Integer spuId;

    /**
     * 商品名
     */
    private String spuName;

    /**
     * 是否上架
     */
    private Integer isOn;

    /**
     * 商品是否过期
     */
    private Integer isDelete;

    /**
     * sku
     */
    private ShoppingCartSKUDTO shoppingCartSKUDTO;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 降价价格
     */
    private Integer subPoints;
}
