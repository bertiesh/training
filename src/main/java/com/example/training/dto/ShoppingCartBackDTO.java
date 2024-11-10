package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartBackDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 昵称
     */
    private String nickname;

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
     * 加入时价格
     */
    private Integer addPoints;

    /**
     * 降价价格
     */
    private Integer subPoints;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
