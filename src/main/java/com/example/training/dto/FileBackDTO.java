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
public class FileBackDTO {
    /**
     * file id
     */
    private Integer id;

    /**
     * fileName
     */
    private String fileName;

    /**
     * file description
     */
    private String fileDesc;

    /**
     * file address
     */
    private String fileSrc;
}
