package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.dto.ProjectBackDTO;
import com.example.training.dto.TemplateBackDTO;
import com.example.training.entity.Project;
import com.example.training.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface ProjectDao extends BaseMapper<Project> {
    /**
     * get backend projects
     *
     * @param current   page
     * @param size      size
     * @param condition condition
     * @return {@link List < ProjectBackDTO >} projects
     */
    List<ProjectBackDTO> listProjectBacks(@Param("current") Long current, @Param("size") Long size,
                                          @Param("condition") ConditionVO condition);
}
