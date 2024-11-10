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
public class SPUHomeDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * spu名称
     */
    private String name;

    /**
     * 封面图
     */
    private String cover;

    /**
     * 最低积分价格
     */
    private Integer points;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 销量
     */
    private Integer salesCount;
}
