package com.example.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.training.dto.TemplateBackDTO;
import com.example.training.entity.Template;
import com.example.training.vo.ConditionVO;
import com.example.training.vo.PageResult;
import com.example.training.vo.Result;
import com.example.training.vo.TemplateVO;

/**
 * @author Xuxinyuan
 */
public interface TemplateService extends IService<Template> {
    /**
     * 保存或更新模板
     *
     * @param templateVO 模板信息
     */
    void saveOrUpdateTemplate(TemplateVO templateVO);

    /**
     * 查看后台模板列表
     *
     * @param condition 条件
     * @return {@link Result< TemplateBackDTO >} 模板列表
     */
    PageResult<TemplateBackDTO> listTemplateBacks(ConditionVO condition);

    /**
     * 根据id获取后台模板信息
     * @param templateId 模板id
     * @return {@link Result}模板信息
     */
    TemplateBackDTO getTemplateBackById(Integer templateId);
}
