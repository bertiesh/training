package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreightDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * freightCode
     */
    private String freightCode;

    /**
     * freightBrand
     */
    private String freightBrand;

    /**
     * shipping time
     */
    private LocalDateTime createTime;
}
