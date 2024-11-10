package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileCollectionBackDTO {
    /**
     * file collection id
     */
    private Integer id;

    /**
     * collectionName
     */
    private String collectionName;

    /**
     * collection description
     */
    private String collectionDesc;

    /**
     * collectionCover
     */
    private String collectionCover;

    /**
     * fileCount
     */
    private Integer fileCount;

    /**
     * status 1public 2private
     */
    private Integer status;

    /**
     * project ids
     */
    private String projectIds;

    /**
     * projects
     */
    private List<ProjectBackDTO> projects;
}
