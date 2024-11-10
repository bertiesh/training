package com.example.training.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class HelpBackDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * Customer service question
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

    /**
     * question keywords
     */
    private String standardQuestionFeatures;

    /**
     * question keywords
     */
    private List<String> standardQuestionFeaturesList;

    /**
     * synonymicQuestions
     */
    private String synonymicQuestions;

    /**
     * synonymicQuestions keywords
     */
    private String synonymicQuestionsFeatures;

    /**
     * synonymicQuestions keywords
     */
    private List<String> synonymicQuestionsFeaturesList;

    /**
     * is common
     */
    private Boolean status;

    /**
     * isDelete
     */
    private Boolean isDelete;
}
