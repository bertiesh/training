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
public class FileInfoDTO {
    /**
     * file id
     */
    private Integer id;

    /**
     * historyCount
     */
    private Integer historyCount;

    /**
     * has read
     */
    private Boolean isHistory;

    /**
     * fileName
     */
    private String fileName;

    /**
     * fileDescription
     */
    private String fileDesc;

    /**
     * file address
     */
    private String fileSrc;
}
