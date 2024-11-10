package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.dto.*;
import com.example.training.entity.Article;
import com.example.training.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface ArticleDao extends BaseMapper<Article> {
    /**
     * get homepage article
     *
     * @param current page
     * @param size    size
     * @return article list
     */
    List<ArticleHomeDTO> listArticles(@Param("current") Long current, @Param("size") Long size);

    /**
     * get article by id
     *
     * @param articleId article id
     * @return article info
     */
    ArticleDTO getArticleById(@Param("articleId") Integer articleId);

    /**
     * get article by condition
     *
     * @param current   page
     * @param size      size
     * @param condition condition
     * @return article list
     */
    List<ArticlePreviewDTO> listArticlesByCondition(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

    /**
     * get backend article
     *
     * @param current   page
     * @param size      size
     * @param condition condition
     * @return article list
     */
    List<ArticleBackDTO> listArticleBacks(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

    /**
     * get number of backend articles
     *
     * @param condition condition
     * @return article num
     */
    Integer countArticleBacks(@Param("condition") ConditionVO condition);

    /**
     * get recommended article
     *
     * @param articleId article id
     * @return article list
     */
    List<ArticleRecommendDTO> listRecommendArticles(@Param("articleId") Integer articleId);

    /**
     * article statistics
     *
     * @return {@link List< ArticleStatisticsDTO >} article statistics
     */
    List<ArticleStatisticsDTO> listArticleStatistics();
}
