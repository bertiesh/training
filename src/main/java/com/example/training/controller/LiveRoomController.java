package com.example.training.controller;

import com.example.training.annotation.OptLog;
import com.example.training.dto.FileCollectionBackDTO;
import com.example.training.dto.FileCollectionDTO;
import com.example.training.dto.LiveRoomBackDTO;
import com.example.training.dto.LiveRoomDTO;
import com.example.training.entity.LiveRoom;
import com.example.training.enums.FilePathEnum;
import com.example.training.service.LiveRoomService;
import com.example.training.strategy.context.UploadStrategyContext;
import com.example.training.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static com.example.training.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * @author Xinyuan Xu
 */
@Api(tags = "直播间模块")
@RestController
public class LiveRoomController {
    @Autowired
    private UploadStrategyContext uploadStrategyContext;
    @Autowired
    private LiveRoomService liveRoomService;

    /**
     * upload live cover
     *
     * @param file file
     * @return {@link Result <String>} live cover address
     */
    @ApiOperation(value = "上传直播封面")
    @ApiImplicitParam(name = "file", value = "直播封面", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/lives/cover")
    public Result<String> saveLiveCover(MultipartFile file) {
        return Result.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.LIVE.getPath()));
    }

    /**
     * save/update live
     *
     * @param liveRoomVO live info
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新直播")
    @PostMapping("/admin/lives")
    public Result<?> saveOrUpdateLive(@Valid @RequestBody LiveRoomVO liveRoomVO) {
        liveRoomService.saveOrUpdateLive(liveRoomVO);
        return Result.ok();
    }

    /**
     * search backend live room list
     *
     * @param condition condition
     * @return {@link Result<LiveRoomBackDTO>} live room list
     */
    @ApiOperation(value = "查看后台直播间列表")
    @GetMapping("/admin/lives")
    public Result<PageResult<LiveRoomBackDTO>> listLiveBacks(ConditionVO condition) {
        return Result.ok(liveRoomService.listLiveBacks(condition));
    }

    /**
     * get backend live room info by id
     *
     * @param roomId live room id
     * @return {@link Result} live info
     */
    @ApiOperation(value = "根据id获取后台直播间信息")
    @ApiImplicitParam(name = "roomId", value = "直播间id", required = true, dataType = "Integer")
    @GetMapping("/admin/lives/{roomId}")
    public Result<LiveRoomBackDTO> getLiveBackById(@PathVariable("roomId") Integer roomId) {
        return Result.ok(liveRoomService.getLiveBackById(roomId));
    }

    /**
     * get live info by id
     *
     * @param roomId live room id
     * @return {@link Result} live info
     */
    @ApiOperation(value = "根据id获取直播间信息")
    @ApiImplicitParam(name = "roomId", value = "直播间id", required = true, dataType = "Integer")
    @GetMapping("/lives/{roomId}")
    public Result<LiveRoomDTO> getLiveById(@PathVariable("roomId") Integer roomId) {
        return Result.ok(liveRoomService.getLiveById(roomId));
    }

    /**
     * get live list
     *
     * @param condition condition
     * @return {@link Result<LiveRoomDTO>} live list
     */
    @ApiOperation(value = "获取直播列表")
    @GetMapping("/lives")
    public Result<PageResult<LiveRoomDTO>> listLives(ConditionVO condition) {
        return Result.ok(liveRoomService.listLives(condition));
    }

}
