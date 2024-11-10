package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Xuxinyuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * menu name
     */
    private String name;

    /**
     * path
     */
    private String path;

    /**
     * icon
     */
    private String icon;

    /**
     * createTime
     */
    private LocalDateTime createTime;

    /**
     * sort
     */
    private Integer orderNum;

    /**
     * parent id
     */
    private Integer parentId;

    /**
     * isDisable
     */
    private Integer isDisable;

    /**
     * isHidden
     */
    private Integer isHidden;

    /**
     * children menus
     */
    private List<MenuDTO> children;
}
