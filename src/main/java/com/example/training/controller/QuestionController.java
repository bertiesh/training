package com.example.training.controller;

import com.example.training.annotation.OptLog;
import com.example.training.dto.ArticleBackDTO;
import com.example.training.dto.QuestionBackDTO;
import com.example.training.enums.FilePathEnum;
import com.example.training.service.QuestionService;
import com.example.training.strategy.context.UploadStrategyContext;
import com.example.training.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.io.IOException;
import java.util.List;

import static com.example.training.constant.OptTypeConst.REMOVE;
import static com.example.training.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * @author Xuxinyuan
 */
@Api(tags = "题目模块")
@RestController
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    /**
     * add/update question
     *
     * @param questionVO question info
     * @return {@link Result <Integer>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改题目")
    @PostMapping("/admin/questions")
    public Result<Integer> saveOrUpdateQuestion(@Valid @RequestBody QuestionVO questionVO) {
        return Result.ok(questionService.saveOrUpdateQuestion(questionVO));
    }

    /**
     * delete question
     *
     * @param questionIdList question id list
     * @return {@link Result<>}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理删除题目")
    @DeleteMapping("/admin/questions")
    public Result<?> deleteQuestions(@RequestBody List<Integer> questionIdList) {
        questionService.deleteQuestions(questionIdList);
        return Result.ok();
    }

    /**
     * upload question img
     *
     * @param file img
     * @return {@link Result<String>} img address
     */
    @ApiOperation(value = "上传题目图片")
    @ApiImplicitParam(name = "file", value = "题目图片", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/questions/images")
    public Result<String> saveQuestionImages(MultipartFile file) {
        return Result.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.QUESTION.getPath()));
    }

    /**
     * get backend question
     *
     * @param conditionVO condition
     * @return {@link Result< QuestionBackDTO >} backend question list
     */
    @ApiOperation(value = "查看后台题目")
    @GetMapping("/admin/questions")
    public Result<PageResult<QuestionBackDTO>> listQuestionBacks(ConditionVO conditionVO) {
        return Result.ok(questionService.listQuestionBacks(conditionVO));
    }

    /**
     * get backend question by id
     *
     * @param questionId question id
     * @return {@link Result<QuestionVO>} backend question
     */
    @ApiOperation(value = "根据id查看后台题目")
    @ApiImplicitParam(name = "questionId", value = "题目id", required = true, dataType = "Integer")
    @GetMapping("/admin/questions/{questionId}")
    public Result<QuestionVO> getQuestionBackById(@PathVariable("questionId") Integer questionId) {
        return Result.ok(questionService.getQuestionBackById(questionId));
    }

    /**
     * add/update answer
     *
     * @param answerVO answer info
     * @return {@link Result <>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改题目")
    @PostMapping("/admin/answers")
    public Result<?> saveOrUpdateAnswer(@Valid @RequestBody AnswerVO answerVO) {
        questionService.saveOrUpdateAnswer(answerVO);
        return Result.ok();
    }

    /**
     * get backend answer by id
     *
     * @param answerId answer id
     * @return {@link Result<AnswerVO>} backend answer
     */
    @ApiOperation(value = "根据id查看后台答案")
    @ApiImplicitParam(name = "answerId", value = "答案id", required = true, dataType = "Integer")
    @GetMapping("/admin/answers/{answerId}")
    public Result<AnswerVO> getAnswerBackById(@PathVariable("answerId") Integer answerId) {
        return Result.ok(questionService.getAnswerBackById(answerId));
    }

    /**
     * import question
     *
     * @param file question file
     * @return {@link Result<>}
     */
    @ApiOperation(value = "导入题目表")
    @ApiImplicitParam(name = "file", value = "题目表", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/questions/import")
    public Result<?> importQuestions(MultipartFile file) throws IOException {
        questionService.importQuestions(file);
        return Result.ok();
    }
}
