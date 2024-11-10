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
public class PhotoDTO {
    /**
     * photoAlbumCover
     */
    private String photoAlbumCover;

    /**
     * photoAlbumName
     */
    private String photoAlbumName;

    /**
     * photoList
     */
    private List<String> photoList;
}
