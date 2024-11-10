package com.example.training.controller;

import cn.hutool.core.lang.tree.Tree;
import com.example.training.annotation.OptLog;
import com.example.training.service.RegionService;
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
@Api(tags = "区域模块")
@RestController
public class RegionController {
    @Autowired
    private RegionService regionService;

    /**
     * add/update region
     *
     * @param regionVO region
     * @return {@link Result <>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改行政区划")
    @PostMapping("/admin/regions")
    public Result<?> saveOrUpdateRegions(@Valid @RequestBody RegionVO regionVO) {
        regionService.saveOrUpdateRegions(regionVO);
        return Result.ok();
    }

    /**
     * delete region
     *
     * @param regionCodeList region id list
     * @return {@link Result<>}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理删除行政区划")
    @DeleteMapping("/admin/regions")
    public Result<?> deleteRegions(@RequestBody List<Integer> regionCodeList) {
        regionService.deleteRegions(regionCodeList);
        return Result.ok();
    }

    /**
     * get region list
     *
     * @param conditionVO condition
     * @return {@link Result <Tree>} region list
     */
    @ApiOperation(value = "查看行政区划列表")
    @GetMapping("/regions")
    public Result<List<Tree<String>>> listRegions(ConditionVO conditionVO) {
        return Result.ok(regionService.listRegions(conditionVO));
    }
}
