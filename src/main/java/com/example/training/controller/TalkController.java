package com.example.training.controller;

import com.example.training.dto.TalkBackDTO;
import com.example.training.dto.TalkDTO;
import com.example.training.enums.FilePathEnum;
import com.example.training.service.TalkService;
import com.example.training.strategy.context.UploadStrategyContext;
import com.example.training.vo.ConditionVO;
import com.example.training.vo.PageResult;
import com.example.training.vo.Result;
import com.example.training.vo.TalkVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Api(tags = "讨论模块")
@RestController
public class TalkController {
    @Autowired
    private TalkService talkService;
    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    /**
     * get home talks
     *
     * @return {@link Result <String>}
     */
    @ApiOperation(value = "查看首页讨论")
    @GetMapping("/home/talks")
    public Result<List<String>> listHomeTalks() {
        return Result.ok(talkService.listHomeTalks());
    }

    /**
     * get talk list
     *
     * @return {@link Result<TalkDTO>}
     */
    @ApiOperation(value = "查看讨论列表")
    @GetMapping("/talks")
    public Result<PageResult<TalkDTO>> listTalks(ConditionVO condition) {
        return Result.ok(talkService.listTalks(condition));
    }

    /**
     * get talks by id
     *
     * @param talkId 讨论id
     * @return {@link Result<TalkDTO>}
     */
    @ApiOperation(value = "根据id查看讨论")
    @ApiImplicitParam(name = "talkId", value = "讨论id", required = true, dataType = "Integer")
    @GetMapping("/talks/{talkId}")
    public Result<TalkDTO> getTalkById(@PathVariable("talkId") Integer talkId) {
        return Result.ok(talkService.getTalkById(talkId));
    }

    /**
     * like talk
     *
     * @param talkId talk id
     * @return {@link Result<>}
     */
    @ApiOperation(value = "点赞讨论")
    @ApiImplicitParam(name = "talkId", value = "讨论id", required = true, dataType = "Integer")
    @PostMapping("/talks/{talkId}/like")
    public Result<?> saveTalkLike(@PathVariable("talkId") Integer talkId) {
        talkService.saveTalkLike(talkId);
        return Result.ok();
    }

    /**
     * upload talk cover
     *
     * @param file img
     * @return {@link Result<String>} talk cover address
     */
    @ApiOperation(value = "上传讨论图片")
    @ApiImplicitParam(name = "file", value = "讨论图片", required = true, dataType = "MultipartFile[]")
    @PostMapping("/admin/talks/images")
    public Result<List<String>> saveTalkImages(MultipartFile[] file) {
        return Result.ok(uploadStrategyContext.executeUploadMultipleStrategy(file, FilePathEnum.TALK.getPath()));
    }

    /**
     * save/update talk
     *
     * @param talkVO talk info
     * @return {@link Result<>}
     */
    @ApiOperation(value = "保存或修改讨论")
    @PostMapping("/admin/talks")
    public Result<Integer> saveOrUpdateTalk(@Valid @RequestBody TalkVO talkVO) {
        return Result.ok(talkService.saveOrUpdateTalk(talkVO));
    }

    /**
     * delete talk
     *
     * @param talkIdList talk id list
     * @return {@link Result<>}
     */
    @ApiOperation(value = "删除讨论")
    @DeleteMapping("/admin/talks")
    public Result<?> deleteTalks(@RequestBody List<Integer> talkIdList) {
        talkService.deleteTalks(talkIdList);
        return Result.ok();
    }

    /**
     * get backend talk
     *
     * @param conditionVO condition
     * @return {@link Result<TalkBackDTO>} talk list
     */
    @ApiOperation(value = "查看后台讨论")
    @GetMapping("/admin/talks")
    public Result<PageResult<TalkBackDTO>> listBackTalks(ConditionVO conditionVO) {
        return Result.ok(talkService.listBackTalks(conditionVO));
    }

    /**
     * get backend talk by id
     *
     * @param talkId talk id
     * @return {@link Result<TalkDTO>}
     */
    @ApiOperation(value = "根据id查看后台讨论")
    @ApiImplicitParam(name = "talkId", value = "说说id", required = true, dataType = "Integer")
    @GetMapping("/admin/talks/{talkId}")
    public Result<TalkBackDTO> getBackTalkById(@PathVariable("talkId") Integer talkId) {
        return Result.ok(talkService.getBackTalkById(talkId));
    }
}
