package com.example.training.controller;

import com.example.training.annotation.OptLog;
import com.example.training.dto.*;
import com.example.training.service.ProjectService;
import com.example.training.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.example.training.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * @author Xinyuan Xu
 */
@Api(tags = "项目模块")
@RestController
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    /**
     * save/update project
     *
     * @param projectVO project info
     * @return {@link Result <>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新项目")
    @PostMapping("/admin/projects")
    public Result<?> saveOrUpdateProject(@Valid @RequestBody ProjectVO projectVO) {
        projectService.saveOrUpdateProject(projectVO);
        return Result.ok();
    }

    /**
     * search backend project
     *
     * @param condition condition
     * @return {@link Result<ProjectBackDTO>} project list
     */
    @ApiOperation(value = "查看后台项目列表")
    @GetMapping("/admin/projects")
    public Result<PageResult<ProjectBackDTO>> listProjectBacks(ConditionVO condition) {
        return Result.ok(projectService.listProjectBacks(condition));
    }

    /**
     * get backend project info by id
     *
     * @param projectId project id
     * @return {@link Result} project info
     */
    @ApiOperation(value = "根据id获取后台项目信息")
    @ApiImplicitParam(name = "projectId", value = "项目id", required = true, dataType = "Integer")
    @GetMapping("/admin/project/{projectId}/info")
    public Result<ProjectBackDTO> getProjectBackById(@PathVariable("projectId") Integer projectId) {
        return Result.ok(projectService.getProjectBackById(projectId));
    }

    /**
     * get project info by id
     * @param projectId project id
     * @param password password
     * @return {@link Result} project info
     */
    @ApiOperation(value = "根据id获取项目信息")
    @ApiImplicitParam(name = "projectId", value = "项目id", required = true, dataType = "Integer")
    @GetMapping("/project/{projectId}/info")
    public Result<ProjectDTO> getProjectById(@PathVariable("projectId") Integer projectId,
                                             @RequestParam(required = false, defaultValue = "1") String password) {
        return Result.ok(projectService.getProjectById(projectId, password));
    }

    /**
     * save/update posts
     *
     * @param postVO post info
     * @return {@link Result <>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新回答")
    @PostMapping("/projects/post")
    public Result<?> saveOrUpdateProjectPost(@Valid @RequestBody PostVO postVO) {
        return Result.ok(projectService.saveOrUpdateProjectPost(postVO));
    }

    /**
     * get project info by id
     *
     * @param projectId project id
     * @return {@link Result} project info
     */
    @ApiOperation(value = "根据id获取项目信息")
    @ApiImplicitParam(name = "projectId", value = "项目id", required = true, dataType = "Integer")
    @GetMapping("/project/{projectId}/history")
    public Result<List<QuestionPostDTO>> getProjectHistoryById(@PathVariable("projectId") Integer projectId) {
        return Result.ok(projectService.getProjectHistoryById(projectId));
    }

    /**
     * get backend project info by id
     *
     * @param projectId project id
     * @return {@link Result} project info
     */
    @ApiOperation(value = "管理员根据id获取项目信息")
    @ApiImplicitParam(name = "projectId", value = "项目id", required = true, dataType = "Integer")
    @GetMapping("/admin/project/{projectId}/analysis")
    public Result<List<QuestionBackAnalysisDTO>> getProjectBackAnalysisById(@PathVariable("projectId") Integer projectId) {
        return Result.ok(projectService.getProjectBackAnalysisById(projectId));
    }
}
