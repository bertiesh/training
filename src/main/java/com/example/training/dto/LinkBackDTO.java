package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Xuxinyuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkBackDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * linkName
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
     * link description
     */
    private String linkIntro;

    /**
     * createTime
     */
    private LocalDateTime createTime;
}
