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
public class PhotoAlbumDTO {
    /**
     * album id
     */
    private Integer id;

    /**
     * album name
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
}
