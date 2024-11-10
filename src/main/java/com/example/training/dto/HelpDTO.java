package com.example.training.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class HelpDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * customer service question
     */
    private String question;

    /**
     * customer service answer
     */
    private String answer;

    /**
     * question category
     */
    private String category;

    /**
     * question intention
     */
    private String intention;
}
