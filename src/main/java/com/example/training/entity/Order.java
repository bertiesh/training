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
@TableName("tb_order")
public class Order {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    private String orderCode;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 地址id
     */
    private Integer addressId;

    /**
     * 总积分
     */
    private Integer totalPoints;

    /**
     * 订单状态 1待发货 2待收货 3已完成 4已关闭
     */
    private Integer status;

    /**
     * 物流id
     */
    private String freightIds;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 是否删除
     */
    private Integer isDelete;

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

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
