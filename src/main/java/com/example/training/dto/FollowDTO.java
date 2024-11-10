package com.example.training.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Data
public class FollowDTO {
    /**
     * resource id
     */
    private Integer id;

    /**
     * url
     */
    private String url;

    /**
     * requestMethod
     */
    private String requestMethod;

    /**
     * follows
     */
    private List<Integer> followList;
}
