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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerBackDTO {
    /**
     * questionAnalysis
     */
    private String questionAnalysis;

    /**
     * isDelete
     */
    private Integer isDelete;

    /**
     * images
     */
    private String images;

    /**
     * possibleAnswers
     */
    private String possibleAnswers;

    /**
     * correctAnswers
     */
    private String correctAnswers;

    /**
     * imgList
     */
    private List<String> imgList;

    /**
     * possibleAnswerList
     */
    private List<String> possibleAnswerList;

    /**
     * correctAnswerList
     */
    private List<String> correctAnswerList;
}
