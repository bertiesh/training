package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * link name
     */
    private String linkName;

    /**
     * linkPhoto
     */
    private String linkPhoto;

    /**
     * link url
     */
    private String linkAddress;

    /**
     * description
     */
    private String linkIntro;
}
