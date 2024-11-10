package com.example.training.controller;

import cn.hutool.core.date.DateUtil;
import com.example.training.dto.BackInfoDTO;
import com.example.training.dto.HomeInfoDTO;
import com.example.training.dto.WebsiteDTO;
import com.example.training.enums.FilePathEnum;
import com.example.training.service.InfoService;
import com.example.training.service.impl.WebSocketServiceImpl;
import com.example.training.strategy.context.UploadStrategyContext;
import com.example.training.vo.Result;
import com.example.training.vo.VoiceVO;
import com.example.training.vo.WebsiteConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

/**
 * @author Xinyuan Xu
 */
@Api(tags = "信息模块")
@RestController
public class InfoController {
    @Autowired
    private InfoService infoService;
    @Autowired
    private WebSocketServiceImpl webSocketService;
    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    /**
     * search profile
     *
     * @return {@link Result < HomeInfoDTO >} profile info
     */
    @ApiOperation(value = "查看个人主页信息")
    @GetMapping("/")
    public Result<HomeInfoDTO> getBlogHomeInfo(@RequestParam Integer userInfoId) {
        return Result.ok(infoService.getBlogHomeInfo(userInfoId));
    }

    /**
     * search backend info
     *
     * @param start start date
     * @param end end date
     * @return {@link Result<BackInfoDTO>} backend info
     */
    @ApiOperation(value = "查看后台信息")
    @GetMapping("/admin")
    public Result<BackInfoDTO> getBlogBackInfo(@RequestParam(required = false) String start,
                                               @RequestParam(required = false) String end) {
        return Result.ok(infoService.getBlogBackInfo(start, end));
    }

    /**
     * upload config img
     *
     * @param file file
     * @return {@link Result<String>} config img
     */
    @ApiOperation(value = "上传配置图片")
    @ApiImplicitParam(name = "file", value = "图片", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/config/images")
    public Result<String> savePhotoAlbumCover(MultipartFile file) {
        return Result.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.CONFIG.getPath()));
    }

    /**
     * update web config
     *
     * @param websiteConfigVO web config info
     * @return {@link Result}
     */
    @ApiOperation(value = "更新网站配置")
    @PutMapping("/admin/website/config")
    public Result<?> updateWebsiteConfig(@Valid @RequestBody WebsiteConfigVO websiteConfigVO) {
        infoService.updateWebsiteConfig(websiteConfigVO);
        return Result.ok();
    }

    /**
     * get web config
     *
     * @return {@link Result<WebsiteConfigVO>} web config
     */
    @ApiOperation(value = "获取网站配置")
    @GetMapping("/admin/website/config")
    public Result<WebsiteConfigVO> getWebsiteConfig() {
        return Result.ok(infoService.getWebsiteConfig());
    }

    /**
     * save audio message
     *
     * @param voiceVO audio message
     * @return {@link Result<String>} audio address
     */
    @ApiOperation(value = "上传语音")
    @PostMapping("/voice")
    public Result<String> sendVoice(VoiceVO voiceVO) {
        webSocketService.sendVoice(voiceVO);
        return Result.ok();
    }

    /**
     * upload visitor info
     *
     * @return {@link Result<WebsiteDTO>}
     */
    @GetMapping("/report")
    public Result<WebsiteDTO> report() {
        return Result.ok(infoService.report());
    }
}
