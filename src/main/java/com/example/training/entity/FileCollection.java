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
@TableName(value ="tb_file_collection")
public class FileCollection {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文档合集名
     */
    private String collectionName;

    /**
     * 文档合集描述
     */
    private String collectionDesc;

    /**
     * 文档合集封面
     */
    private String collectionCover;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 状态值 1公开 2私密
     */
    private Integer status;

    /**
     * 文件类型
     */
    private Integer type;

    /**
     * 练习合集id
     */
    private String projectIds;

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
