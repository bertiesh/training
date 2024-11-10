package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.dto.TagBackDTO;
import com.example.training.entity.Tag;
import com.example.training.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface TagDao extends BaseMapper<Tag> {
    /**
     * get backend tags
     *
     * @param current   page
     * @param size      size
     * @param condition condition
     * @return {@link List <TagBackDTO>} tags
     */
    List<TagBackDTO> listTagBackDTO(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

    /**
     * get tags by article id
     *
     * @param articleId article id
     * @return {@link List<String>} tags
     */
    List<String> listTagNameByArticleId(Integer articleId);

    /**
     * get tags by question id
     *
     * @param questionId question id
     * @return {@link List<String>} tags
     */
    List<String> listTagNameByQuestionId(Integer questionId);
}
