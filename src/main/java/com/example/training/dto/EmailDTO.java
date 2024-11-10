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
public class EmailDTO {
    /**
     * email
     */
    private String email;

    /**
     * subject
     */
    private String subject;

    /**
     * content
     */
    private String content;
}
