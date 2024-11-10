package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.dto.QuestionBackDTO;
import com.example.training.entity.Question;
import com.example.training.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface QuestionDao extends BaseMapper<Question> {
    /**
     * get backend questions
     *
     * @param current   page
     * @param size      size
     * @param condition condition
     * @return questions
     */
    List<QuestionBackDTO> listQuestionBacks(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

    /**
     * get backend question count
     *
     * @param condition condition
     * @return question count
     */
    Integer countQuestionBacks(@Param("condition") ConditionVO condition);
}
