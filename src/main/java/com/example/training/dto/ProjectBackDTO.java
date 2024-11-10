package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectBackDTO {
    /**
     * project id
     */
    private Integer id;

    /**
     * project name
     */
    private String name;

    /**
     * description
     */
    private String description;

    /**
     * cover
     */
    private String cover;

    /**
     * isDelete
     */
    private Boolean isDelete;

    /**
     * status 1 questionnaire 2 test
     */
    private Integer status;

    /**
     * questions
     */
    private String questions;

    /**
     * questions
     */
    private List<QuestionBackDTO> questionBackDTOS;

    /**
     * cascades
     */
    private String cascades;

    /**
     * cascades
     */
    private Map<Integer, Map<Integer, Integer>> cascadesMapList;

    /**
     * isActive
     */
    private Boolean isActive;

    /**
     * isRandom
     */
    private Boolean isRandom;

    /**
     * whether to display question number
     */
    private Boolean questionNumber;

    /**
     * password
     */
    private String password;

    /**
     * need a password?
     */
    private Boolean isPassword;

    /**
     * whether to display the answering progress
     */
    private Boolean progressBar;

    /**
     * whether to automatically save selections
     */
    private Boolean autoSave;

    /**
     * is the answer sheet visible?
     */
    private Boolean answerSheetVisible;

    /**
     * Is copying allowed?
     */
    private Boolean copyEnabled;

    /**
     * Is it allowed to change the answer?
     */
    private Boolean enableUpdate;

    /**
     * Whether to display answers and analysis
     */
    private Boolean answerAnalysis;

    /**
     * Random tag list
     */
    private String tagIds;

    /**
     * random question num
     */
    private Integer questionNum;

    /**
     * random question type
     */
    private String types;

    /**
     * startTime
     */
    private LocalDateTime startTime;

    /**
     * endTime
     */
    private LocalDateTime endTime;
}
