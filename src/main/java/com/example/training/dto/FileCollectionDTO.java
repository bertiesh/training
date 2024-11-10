package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xinyuan Xu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileCollectionDTO {
    /**
     * collection id
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
     * collection cover
     */
    private String collectionCover;

    /**
     * collection views
     */
    private Integer viewsCount;

    /**
     * fileCount
     */
    private Integer fileCount;

    /**
     * price
     */
    private Integer status;

    /**
     * notPurchased
     */
    private Integer notPurchased;
}
