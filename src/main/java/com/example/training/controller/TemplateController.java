package com.example.training.controller;

import com.example.training.annotation.OptLog;
import com.example.training.dto.TemplateBackDTO;
import com.example.training.service.TemplateService;
import com.example.training.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.training.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * @author Xinyuan Xu
 */
@Api(tags = "模板模块")
@RestController
public class TemplateController {
    @Autowired
    private TemplateService templateService;

    /**
     * save/update template
     *
     * @param templateVO template info
     * @return {@link Result <>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新模板")
    @PostMapping("/admin/templates")
    public Result<?> saveOrUpdateTemplate(@Valid @RequestBody TemplateVO templateVO) {
        templateService.saveOrUpdateTemplate(templateVO);
        return Result.ok();
    }

    /**
     * get backend template
     *
     * @param condition condition
     * @return {@link Result< TemplateBackDTO >} template list
     */
    @ApiOperation(value = "查看后台模板列表")
    @GetMapping("/admin/templates")
    public Result<PageResult<TemplateBackDTO>> listTemplateBacks(ConditionVO condition) {
        return Result.ok(templateService.listTemplateBacks(condition));
    }

    /**
     * get backend template by id
     *
     * @param templateId template id
     * @return {@link Result} template info
     */
    @ApiOperation(value = "根据id获取后台模板信息")
    @ApiImplicitParam(name = "templateId", value = "模板id", required = true, dataType = "Integer")
    @GetMapping("/admin/template/{templateId}/info")
    public Result<TemplateBackDTO> getTemplateBackById(@PathVariable("templateId") Integer templateId) {
        return Result.ok(templateService.getTemplateBackById(templateId));
    }
}
