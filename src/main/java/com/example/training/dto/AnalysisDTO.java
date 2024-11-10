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
public class AnalysisDTO {
    /**
     * possible answers
     */
    private String possibleAnswers;

    /**
     * possible answer list
     */
    private List<Map<String, Double>> possibleAnswerList;

    /**
     * correctAnswers
     */
    private String correctAnswers;

    /**
     * correctAnswerList
     */
    private List<String> correctAnswerList;

    /**
     * correctness
     */
    private Double correctness;

    /**
     * Statistics on fill-in-the-blank and scoring questions
     */
    private Map<String, Integer> submits;
}
