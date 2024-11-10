package com.example.training.controller;

import com.example.training.util.VideoStreamUtils;
import com.example.training.vo.Result;
import com.example.training.vo.VideoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * @author Xinyuan Xu
 */
@Api(tags = "视频直播模块")
@RestController
public class VideoController {
    /**
     * play video
     *
     * @param videoVO video
     * @return {@link Result <String>}
     */
    @ApiOperation(value = "播放视频")
    @PostMapping("/video/play")
    public Result<String> playVideo(@Valid @RequestBody VideoVO videoVO) {
        return Result.ok(VideoStreamUtils.play(videoVO));
    }

    /**
     * stop video
     *
     * @param videoURI video uri
     */
    @ApiOperation(value = "暂停视频")
    @GetMapping("/video/stop")
    public Result<?> stopVideo(String videoURI) {
        VideoStreamUtils.destroy(videoURI);
        return Result.ok();
    }
}
