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
public class FileDTO {
    /**
     * fileCollectionCover
     */
    private String fileCollectionCover;

    /**
     * fileCollectionName
     */
    private String fileCollectionName;

    /**
     * fileCollectionDescription
     */
    private String collectionDesc;

    /**
     * fileList
     */
    private List<String> fileList;

    /**
     * file count
     */
    private Integer count;

    /**
     * notPurchased
     */
    private Integer notPurchased;

    /**
     * projectIdList
     */
    private List<Integer> projectIdList;

    /**
     * file type
     */
    private Integer type;

    /**
     * collection views count
     */
    private Integer viewsCount;

    /**
     * file list
     */
    private List<FileInfoDTO> fileInfoList;

    /**
     * file recommend list
     */
    private List<FileCollectionDTO> fileRecommendInfoList;
}
