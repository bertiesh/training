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
public class PhotoAlbumBackDTO {
    /**
     * album id
     */
    private Integer id;

    /**
     * albumName
     */
    private String albumName;

    /**
     * albumDescription
     */
    private String albumDesc;

    /**
     * albumCover
     */
    private String albumCover;

    /**
     * photoCount
     */
    private Integer photoCount;

    /**
     * status 1public 2private
     */
    private Integer status;
}
