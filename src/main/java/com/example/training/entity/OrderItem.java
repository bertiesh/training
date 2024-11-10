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
@TableName("tb_order_item")
public class OrderItem {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * spu_id
     */
    private Integer spuId;

    /**
     * sku id
     */
    private Integer skuId;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 单价
     */
    private Integer points;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 状态 订单状态 1待发货 2待收货 3已完成 4已关闭
     */
    private Integer status;
}
