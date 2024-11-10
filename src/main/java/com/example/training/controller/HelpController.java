package com.example.training.controller;

import com.example.training.annotation.OptLog;
import com.example.training.dto.HelpBackDTO;
import com.example.training.dto.HelpDTO;
import com.example.training.service.HelpService;
import com.example.training.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.example.training.constant.OptTypeConst.REMOVE;
import static com.example.training.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * @author Xinyuan Xu
 */
@Api(tags = "帮助模块")
@RestController
public class HelpController {
    @Autowired
    private HelpService helpService;

    /**
     * add/update help
     *
     * @param helpVO help info
     * @return {@link Result <>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改帮助")
    @PostMapping("/admin/helps")
    public Result<?> saveOrUpdateHelps(@Valid @RequestBody HelpVO helpVO) {
        helpService.saveOrUpdateHelps(helpVO);
        return Result.ok();
    }

    /**
     * delete help
     *
     * @param helpIdList help id list
     * @return {@link Result<>}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理删除帮助")
    @DeleteMapping("/admin/helps")
    public Result<?> deleteHelps(@RequestBody List<Integer> helpIdList) {
        helpService.deleteHelps(helpIdList);
        return Result.ok();
    }

    /**
     * search backend help
     *
     * @param conditionVO condition
     * @return {@link Result<HelpBackDTO>} backend help list
     */
    @ApiOperation(value = "查看后台帮助")
    @GetMapping("/admin/helps")
    public Result<PageResult<HelpBackDTO>> listHelpBacks(ConditionVO conditionVO) {
        return Result.ok(helpService.listHelpBacks(conditionVO));
    }

    /**
     * search common help
     *
     * @param conditionVO condition
     * @return {@link Result<HelpDTO>} common help list
     */
    @ApiOperation(value = "查看常见问题")
    @GetMapping("/helps")
    public Result<List<HelpDTO>> listHelps(ConditionVO conditionVO) {
        return Result.ok(helpService.listHelps(conditionVO));
    }

    /**
     * send/delete message
     *
     * @param messageVO help info
     * @return {@link Result <HelpRecordDTO>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "发送删除消息")
    @PostMapping("/helps")
    public Result<?> saveOrUpdateHelpRecords(@Valid @RequestBody MessageVO messageVO) {
        helpService.saveOrUpdateHelpRecords(messageVO);
        return Result.ok();
    }
}
