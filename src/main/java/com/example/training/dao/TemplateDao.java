package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.dto.FileCollectionBackDTO;
import com.example.training.dto.TemplateBackDTO;
import com.example.training.entity.Template;
import com.example.training.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface TemplateDao extends BaseMapper<Template> {
    /**
     * get backend templates
     *
     * @param current   page
     * @param size      size
     * @param condition condition
     * @return {@link List < FileCollectionBackDTO >} templates
     */
    List<TemplateBackDTO> listTemplateBacks(@Param("current") Long current, @Param("size") Long size,
                                            @Param("condition") ConditionVO condition);
}
