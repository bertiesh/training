package com.example.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.training.dto.QuestionBackDTO;
import com.example.training.entity.Question;
import com.example.training.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Xuxinyuan
 */
public interface QuestionService extends IService<Question> {
    /**
     * 添加或修改问题
     *
     * @param questionVO 题目信息
     * @return id
     */
    Integer saveOrUpdateQuestion(QuestionVO questionVO);

    /**
     * 删除题目
     *
     * @param questionIdList 题目id列表
     */
    void deleteQuestions(List<Integer> questionIdList);

    /**
     * 查看后台题目
     * @param conditionVO 条件
     * @return {@link Result< QuestionBackDTO >} 后台题目列表
     */
    PageResult<QuestionBackDTO> listQuestionBacks(ConditionVO conditionVO);

    /**
     * 根据id查看后台题目
     * @param questionId 题目id
     * @return {@link Result<QuestionVO>} 后台题目
     */
    QuestionVO getQuestionBackById(Integer questionId);

    /**
     * 添加或修改答案
     *
     * @param answerVO 答案信息
     */
    void saveOrUpdateAnswer(AnswerVO answerVO);

    /**
     * 根据id查看后台答案
     * @param answerId 答案id
     * @return {@link Result<AnswerVO>} 后台答案
     */
    AnswerVO getAnswerBackById(Integer answerId);

    /**
     * 题目导入
     *
     * @param file 题目表
     */
    void importQuestions(MultipartFile file) throws IOException;
}
