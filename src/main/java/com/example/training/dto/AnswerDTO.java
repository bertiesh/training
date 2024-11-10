package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDTO {
    /**
     * questionAnalysis
     */
    private String questionAnalysis;

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
    private List<Map<String, Double>> possibleAnswerList;

    /**
     * correctAnswerList
     */
    private List<String> correctAnswerList;

    /**
     * myAnswers
     */
    private List<String> myAnswers;

    /**
     * isCorrect
     */
    private Boolean isCorrect;
}
