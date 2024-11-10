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
public class OrderItemDTO {
    /**
     * spu_id
     */
    private Integer spuId;

    /**
     * spu name
     */
    private String name;

    /**
     * sku id
     */
    private Integer skuId;

    /**
     * style specifications
     */
    private String skuName;

    /**
     * style img
     */
    private String picture;

    /**
     * number
     */
    private Integer num;

    /**
     * unit price
     */
    private Integer points;
}
