package com.example.training.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.constant.CommonConst;
import com.example.training.dao.*;
import com.example.training.dto.*;
import com.example.training.entity.*;
import com.example.training.exception.BizException;
import com.example.training.service.TemplateService;
import com.example.training.util.BeanCopyUtils;
import com.example.training.util.CommonUtils;
import com.example.training.util.PageUtils;
import com.example.training.vo.ConditionVO;
import com.example.training.vo.FileCollectionVO;
import com.example.training.vo.PageResult;
import com.example.training.vo.TemplateVO;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Xuxinyuan
 */
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateDao, Template> implements TemplateService {
    @Autowired
    private TemplateDao templateDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private AnswerDao answerDao;
    @Autowired
    private TagDao tagDao;
    @Autowired
    private QuestionTagDao questionTagDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateTemplate(TemplateVO templateVO) {
        // 查询模板名是否存在
        Template template = templateDao.selectOne(new LambdaQueryWrapper<Template>()
                .select(Template::getId)
                .eq(Template::getTemplateName, templateVO.getTemplateName()));
        if (Objects.nonNull(template) && !template.getId().equals(templateVO.getId())) {
            throw new BizException("模板名已存在");
        }
        template = BeanCopyUtils.copyObject(templateVO, Template.class);
        this.saveOrUpdate(template);
    }

    @Override
    public PageResult<TemplateBackDTO> listTemplateBacks(ConditionVO condition) {
        // 查询模板数量
        int count = Math.toIntExact(templateDao.selectCount(new LambdaQueryWrapper<Template>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), Template::getTemplateName, condition.getKeywords())
                .eq(Template::getIsDelete, CommonConst.FALSE)
                .eq(Objects.nonNull(condition.getStatus()), Template::getStatus, condition.getStatus())));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询模板信息
        List<TemplateBackDTO> templateBackDTOList = templateDao.listTemplateBacks(PageUtils.getLimitCurrent(),
                PageUtils.getSize(), condition);
        templateBackDTOList.forEach(templateBackDTO -> {
            templateBackDTO = getQuestions(templateBackDTO);
        });
        return new PageResult<>(templateBackDTOList, count);
    }

    @Override
    public TemplateBackDTO getTemplateBackById(Integer templateId) {
        // 查询模板信息
        Template template = templateDao.selectById(templateId);
        TemplateBackDTO templateBackDTO = BeanCopyUtils.copyObject(template, TemplateBackDTO.class);
        return getQuestions(templateBackDTO);
    }

    /**
     * 添加模板中底层的题目、答案、标签嵌套
     * @param templateBackDTO 模板信息
     * @return TemplateBackDTO
     */
    private TemplateBackDTO getQuestions(TemplateBackDTO templateBackDTO) {
        if (Objects.nonNull(templateBackDTO.getQuestions())) {
            List<Integer> list = CommonUtils.castList(JSON.parseObject(templateBackDTO.getQuestions(), List.class), Integer.class);
            List<QuestionBackDTO> questionBackDTOList = new ArrayList<>();
            for (Integer integer : list) {
                QuestionBackDTO questionBackDTO = BeanCopyUtils.copyObject(questionDao.selectById(integer), QuestionBackDTO.class);
                AnswerBackDTO answerBackDTO = BeanCopyUtils.copyObject(answerDao.selectById(integer), AnswerBackDTO.class);
                // 转换可能答案格式
                if (Objects.nonNull(answerBackDTO.getPossibleAnswers())) {
                    answerBackDTO.setPossibleAnswers(StringEscapeUtils.unescapeHtml4(answerBackDTO.getPossibleAnswers()));
                    if (answerBackDTO.getPossibleAnswers().startsWith("[")) {
                        answerBackDTO.setPossibleAnswerList(CommonUtils.castList(JSON.parseObject(answerBackDTO.getPossibleAnswers(),
                                List.class), String.class));
                    }
                }
                // 转换正确答案格式
                if (Objects.nonNull(answerBackDTO.getCorrectAnswers())) {
                    answerBackDTO.setCorrectAnswers(StringEscapeUtils.unescapeHtml4(answerBackDTO.getCorrectAnswers()));
                    answerBackDTO.setCorrectAnswerList(CommonUtils.castList(JSON.parseObject(answerBackDTO.getCorrectAnswers(),
                            List.class), String.class));
                }
                questionBackDTO.setAnswerBackDTO(answerBackDTO);
                List<Integer> idList = new ArrayList<>();
                questionTagDao.selectList(new LambdaQueryWrapper<QuestionTag>().eq(QuestionTag::getQuestionId, integer))
                        .forEach(questionTag -> idList.add(questionTag.getTagId()));
                List<TagDTO> tagDTOList = new ArrayList<>();
                idList.forEach(id -> tagDTOList.add(BeanCopyUtils.copyObject(tagDao.selectById(id), TagDTO.class)));
                questionBackDTO.setTagDTOList(tagDTOList);
                questionBackDTOList.add(questionBackDTO);
            }
            templateBackDTO.setQuestionBackDTOS(questionBackDTOList);
        }
        if (Objects.nonNull(templateBackDTO.getCascades())) {
            Map<Integer, Map<Integer, Integer>> map = JSON.parseObject(templateBackDTO.getCascades(),
                    new TypeReference<HashMap<Integer,HashMap<Integer, Integer>>>() {});
            templateBackDTO.setCascadesMapList(map);
        }
        return templateBackDTO;
    }
}
