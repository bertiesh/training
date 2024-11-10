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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value ="tb_live_room")
public class LiveRoom {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 直播间名
     */
    private String name;

    /**
     * 直播间描述
     */
    private String description;

    /**
     * 主播id
     */
    private Integer userInfoId;

    /**
     * 直播间封面
     */
    private String cover;

    /**
     * 直播类型
     */
    private Integer type;

    /**
     * 屏幕类型
     */
    private Integer screenType;

    /**
     * 是否允许评论
     */
    private Integer displayComment;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 直播url
     */
    private String liveUrl;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

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
}
