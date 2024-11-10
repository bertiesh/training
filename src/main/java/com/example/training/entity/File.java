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
@TableName(value ="tb_file")
public class File {
    /**
     * 文档id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文档合集id
     */
    private Integer collectionId;

    /**
     * 文档名
     */
    private String fileName;

    /**
     * 文档描述
     */
    private String fileDesc;

    /**
     * 文档地址
     */
    private String fileSrc;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 文件类型
     */
    private Integer type;

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
