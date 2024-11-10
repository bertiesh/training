package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.dto.CategoryBackDTO;
import com.example.training.dto.CategoryDTO;
import com.example.training.entity.Category;
import com.example.training.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface CategoryDao extends BaseMapper<Category> {
    /**
     * query classification and number of corresponding articles
     *
     * @return category list
     */
    List<CategoryDTO> listCategoryDTO();

    /**
     * get backend category
     *
     * @param current   page
     * @param size      size
     * @param condition condition
     * @return {@link List<CategoryBackDTO>} category list
     */
    List<CategoryBackDTO> listCategoryBackDTO(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);
}
