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
public class PhotoBackDTO {
    /**
     * photo id
     */
    private Integer id;

    /**
     * photoName
     */
    private String photoName;

    /**
     * photoDescription
     */
    private String photoDesc;

    /**
     * photo address
     */
    private String photoSrc;
}
