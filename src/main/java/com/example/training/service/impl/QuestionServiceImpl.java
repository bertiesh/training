package com.example.training.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.constant.CommonConst;
import com.example.training.constant.RedisPrefixConst;
import com.example.training.dao.AnswerDao;
import com.example.training.dao.QuestionDao;
import com.example.training.dao.QuestionTagDao;
import com.example.training.dao.TagDao;
import com.example.training.dto.AnswerBackDTO;
import com.example.training.dto.ArticleBackDTO;
import com.example.training.dto.QuestionBackDTO;
import com.example.training.entity.*;
import com.example.training.enums.QuestionTypeEnum;
import com.example.training.enums.RoleEnum;
import com.example.training.exception.BizException;
import com.example.training.service.*;
import com.example.training.util.*;
import com.example.training.vo.*;
import org.apache.commons.text.StringEscapeUtils;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.training.constant.CommonConst.DEFAULT_PASSWORD;

/**
 * @author Xinyuan Xu
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionDao, Question> implements QuestionService {
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private QuestionTagDao questionTagDao;
    @Autowired
    private TagDao tagDao;
    @Autowired
    private AnswerDao answerDao;
    @Autowired
    private TagService tagService;
    @Autowired
    private QuestionTagService questionTagService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private UserAuthServiceImpl userAuthService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer saveOrUpdateQuestion(QuestionVO questionVO) {
        // 保存或修改题目
        Question question = BeanCopyUtils.copyObject(questionVO, Question.class);
        question.setUserId(UserUtils.getLoginUser().getUserInfoId());
        this.saveOrUpdate(question);
        // 保存题目标签
        saveQuestionTag(questionVO, question.getId());
        return question.getId();
    }

    /**
     * 保存题目标签
     *
     * @param questionVO 题目信息
     */
    private void saveQuestionTag(QuestionVO questionVO, Integer questionId) {
        // 编辑题目则删除题目所有标签
        if (Objects.nonNull(questionVO.getId())) {
            questionTagDao.delete(new LambdaQueryWrapper<QuestionTag>().eq(QuestionTag::getQuestionId, questionVO.getId()));
        }
        // 添加题目标签
        List<String> tagNameList = questionVO.getTagNameList();
        if (CollectionUtils.isNotEmpty(tagNameList)) {
            // 查询已存在的标签
            List<Tag> existTagList = tagService.list(new LambdaQueryWrapper<Tag>().in(Tag::getTagName, tagNameList));
            List<String> existTagNameList = existTagList.stream().map(Tag::getTagName).collect(Collectors.toList());
            List<Integer> existTagIdList = existTagList.stream().map(Tag::getId).collect(Collectors.toList());
            // 对比新增不存在的标签
            tagNameList.removeAll(existTagNameList);
            if (CollectionUtils.isNotEmpty(tagNameList)) {
                List<Tag> tagList = tagNameList.stream().map(item -> Tag.builder().tagName(item).build()).collect(Collectors.toList());
                tagService.saveBatch(tagList);
                List<Integer> tagIdList = tagList.stream().map(Tag::getId).collect(Collectors.toList());
                existTagIdList.addAll(tagIdList);
            }
            // 提取标签id绑定题目
            List<QuestionTag> questionTagList = existTagIdList.stream().map(item -> QuestionTag.builder()
                            .questionId(questionId)
                            .tagId(item)
                            .build())
                    .collect(Collectors.toList());
            questionTagService.saveBatch(questionTagList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteQuestions(List<Integer> questionIdList) {
        // 删除题目标签关联
        questionTagDao.delete(new LambdaQueryWrapper<QuestionTag>().in(QuestionTag::getQuestionId, questionIdList));
        // 删除题目
        questionDao.deleteBatchIds(questionIdList);
    }

    @Override
    public PageResult<QuestionBackDTO> listQuestionBacks(ConditionVO condition) {
        // 查询题目总量
        Integer count = questionDao.countQuestionBacks(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台题目
        List<QuestionBackDTO> questionBackDTOList = questionDao.listQuestionBacks(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        if (Objects.isNull(condition.getSize())) {
            questionBackDTOList = questionDao.listQuestionBacks(0L, Long.valueOf(count), condition);
        }
        questionBackDTOList.forEach(item -> {
            // 转换图片格式
            if (Objects.nonNull(item.getImages())) {
                item.setImgList(CommonUtils.castList(JSON.parseObject(item.getImages(), List.class), String.class));
            }
            // 转换关联题目格式
            if (Objects.nonNull(item.getQuestions())) {
                item.setQuestionList(CommonUtils.castList(JSON.parseObject(item.getQuestions(), List.class), Integer.class));
            }
            // 获取答案
            if (Objects.nonNull(answerDao.selectById(item.getId()))) {
                AnswerBackDTO answerBackDTO = BeanCopyUtils.copyObject(answerDao.selectById(item.getId()), AnswerBackDTO.class);
                // 转换图片格式
                if (Objects.nonNull(answerBackDTO.getImages())) {
                    answerBackDTO.setImgList(CommonUtils.castList(JSON.parseObject(answerBackDTO.getImages(), List.class), String.class));
                }
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
                item.setAnswerBackDTO(answerBackDTO);
            }
        });
        return new PageResult<>(questionBackDTOList, count);
    }

    @Override
    public QuestionVO getQuestionBackById(Integer questionId) {
        // 查询题目信息
        Question question = questionDao.selectById(questionId);
        // 查询题目标签
        List<String> tagNameList = tagDao.listTagNameByQuestionId(questionId);
        // 封装数据
        QuestionVO questionVO = BeanCopyUtils.copyObject(question, QuestionVO.class);
        questionVO.setTagNameList(tagNameList);
        return questionVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateAnswer(AnswerVO answerVO) {
        // 保存或修改答案
        if (Objects.nonNull(answerVO.getPossibleAnswers())) {
            answerVO.setPossibleAnswers(StringEscapeUtils.escapeHtml4(answerVO.getPossibleAnswers()));
        }
        if (Objects.nonNull(answerVO.getCorrectAnswers())) {
            answerVO.setCorrectAnswers(StringEscapeUtils.escapeHtml4(answerVO.getCorrectAnswers()));
        }
        Answer answer = BeanCopyUtils.copyObject(answerVO, Answer.class);
        answer.setUserId(UserUtils.getLoginUser().getUserInfoId());
        answerService.saveOrUpdate(answer);
    }

    @Override
    public AnswerVO getAnswerBackById(Integer answerId) {
        // 查询答案信息
        Answer answer = answerDao.selectById(answerId);
        if (Objects.isNull(answer)) {
            return new AnswerVO();
        }
        if (Objects.nonNull(answer.getCorrectAnswers())) {
            answer.setCorrectAnswers(StringEscapeUtils.unescapeHtml4(answer.getCorrectAnswers()));
        }
        if (Objects.nonNull(answer.getPossibleAnswers())) {
            answer.setPossibleAnswers(StringEscapeUtils.unescapeHtml4(answer.getPossibleAnswers()));
        }
        // 封装数据
        return BeanCopyUtils.copyObject(answer, AnswerVO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importQuestions(MultipartFile file) throws IOException {
        //获取excel表
        InputStream inputStream = file.getInputStream();
        ReadableWorkbook readableWorkbook = new ReadableWorkbook(inputStream);
        readableWorkbook.getSheets().forEach(sheet -> {
            try {
                Stream<Row> rows = sheet.openStream();
                rows.forEach(row -> {
                    if (row.getRowNum() == 1) {
                        return;
                    }
                    // 新增问题信息
                    Question question = Question.builder()
                            .userId(UserUtils.getLoginUser().getUserInfoId())
                            .questionTitle(userAuthService.getCellValue(row, 0).orElse("?"))
                            .questionDescription(userAuthService.getCellValue(row, 1).orElse(null))
                            .type(Integer.valueOf(userAuthService.getCellValue(row, 2).orElse("0")))
                            .status(Integer.valueOf(userAuthService.getCellValue(row, 3).orElse("0")))
                            .build();
                    questionDao.insert(question);
                    // 绑定标签
                    QuestionTag questionTag = QuestionTag.builder().questionId(question.getId()).tagId(1).build();
                    questionTagDao.insert(questionTag);
                    // 新增回答
                    Answer answer = Answer.builder()
                            .id(question.getId())
                            .userId(UserUtils.getLoginUser().getUserInfoId())
                            .questionAnalysis(userAuthService.getCellValue(row, 4).orElse(null))
                            .type(question.getType())
                            .status(question.getStatus())
                            .possibleAnswers(userAuthService.getCellValue(row, 5).orElse(null))
                            .correctAnswers(userAuthService.getCellValue(row, 6).orElse(null))
                            .build();
                    answerDao.insert(answer);
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
