package com.example.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.training.dto.ProjectBackDTO;
import com.example.training.dto.ProjectDTO;
import com.example.training.dto.QuestionBackAnalysisDTO;
import com.example.training.dto.QuestionPostDTO;
import com.example.training.entity.Project;
import com.example.training.vo.*;

import java.util.List;

/**
 * @author Xuxinyuan
 */
public interface ProjectService extends IService<Project> {
    /**
     * 保存或更新项目
     *
     * @param projectVO 项目信息
     */
    void saveOrUpdateProject(ProjectVO projectVO);

    /**
     * 查看后台项目列表
     *
     * @param condition 条件
     * @return {@link Result<  ProjectBackDTO  >} 项目列表
     */
    PageResult<ProjectBackDTO> listProjectBacks(ConditionVO condition);

    /**
     * 根据id获取后台项目信息
     * @param projectId 项目id
     * @return {@link Result}项目信息
     */
    ProjectBackDTO getProjectBackById(Integer projectId);

    /**
     * 根据id获取项目信息
     * @param projectId 项目id
     * @param password 密码
     * @return {@link Result}项目信息
     */
    ProjectDTO getProjectById(Integer projectId, String password);

    /**
     * 保存或更新回答
     *
     * @param postVO 回答信息
     * @return {@link QuestionPostDTO}
     */
    List<QuestionPostDTO> saveOrUpdateProjectPost(PostVO postVO);

    /**
     * 根据id获取项目信息
     * @param projectId 项目id
     * @return {@link Result}项目信息
     */
    List<QuestionPostDTO> getProjectHistoryById(Integer projectId);

    /**
     * 管理员根据id获取项目信息
     * @param projectId 项目id
     * @return {@link Result}项目信息
     */
    List<QuestionBackAnalysisDTO> getProjectBackAnalysisById(Integer projectId);
}
