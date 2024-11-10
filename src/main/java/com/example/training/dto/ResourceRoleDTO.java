package com.example.training.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Data
public class ResourceRoleDTO {
    /**
     * 资源id
     */
    private Integer id;

    /**
     * 路径
     */
    private String url;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 角色名
     */
    private List<String> roleList;
}
