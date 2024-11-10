package com.example.training.controller;

import com.example.training.annotation.OptLog;
import com.example.training.dto.*;
import com.example.training.enums.FilePathEnum;
import com.example.training.service.ArticleService;
import com.example.training.strategy.context.ArticleImportStrategyContext;
import com.example.training.strategy.context.UploadStrategyContext;
import com.example.training.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static com.example.training.constant.OptTypeConst.*;

/**
 * @author Xinyuan Xu
 */
@Api(tags = "文章模块")
@RestController
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private UploadStrategyContext uploadStrategyContext;
    @Autowired
    private ArticleImportStrategyContext articleImportStrategyContext;

    /**
     * search article title
     *
     * @return {@link Result <ArchiveDTO>} article title list
     */
    @ApiOperation(value = "查看文章标题")
    @GetMapping("/articles/archives")
    public Result<PageResult<ArchiveDTO>> listArchives(ConditionVO conditionVO) {
        return Result.ok(articleService.listArchives(conditionVO));
    }

    /**
     * search article num
     *
     * @return {@link Result<Integer>} article num list
     */
    @ApiOperation(value = "查看文章条数")
    @GetMapping("/articles")
    public Result<List<Integer>> listArticles() {
        return Result.ok(articleService.listArticles());
    }

    /**
     * search backend article
     *
     * @param conditionVO condition
     * @return {@link Result<ArticleBackDTO>} backend article list
     */
    @ApiOperation(value = "查看后台文章")
    @GetMapping("/admin/articles")
    public Result<PageResult<ArticleBackDTO>> listArticleBacks(ConditionVO conditionVO) {
        return Result.ok(articleService.listArticleBacks(conditionVO));
    }

    /**
     * add/update article
     *
     * @param articleVO article info
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改文章")
    @PostMapping("/articles")
    public Result<?> saveOrUpdateArticle(@Valid @RequestBody ArticleVO articleVO) {
        articleService.saveOrUpdateArticle(articleVO);
        return Result.ok();
    }

    /**
     * modify article review status
     *
     * @param articleTopVO article review info
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改文章审核")
    @PutMapping("/admin/articles/review")
    public Result<?> updateArticleTop(@Valid @RequestBody ArticleTopVO articleTopVO) {
        articleService.updateArticleTop(articleTopVO);
        return Result.ok();
    }

    /**
     * Restore or delete article
     *
     * @param deleteVO delete info
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "恢复或删除文章")
    @PutMapping("/admin/articles")
    public Result<?> updateArticleDelete(@Valid @RequestBody DeleteVO deleteVO) {
        articleService.updateArticleDelete(deleteVO);
        return Result.ok();
    }

    /**
     * upload article img
     *
     * @param file img
     * @return {@link Result<String>} img address
     */
    @ApiOperation(value = "上传文章图片")
    @ApiImplicitParam(name = "file", value = "文章图片", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/articles/images")
    public Result<String> saveArticleImages(MultipartFile file) {
        return Result.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.ARTICLE.getPath()));
    }

    /**
     * delete article
     *
     * @param articleIdList article id list
     * @return {@link Result<>}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理删除文章")
    @DeleteMapping("/admin/articles")
    public Result<?> deleteArticles(@RequestBody List<Integer> articleIdList) {
        articleService.deleteArticles(articleIdList);
        return Result.ok();
    }

    /**
     * search article through is
     *
     * @param articleId article id
     * @return {@link Result<ArticleVO>} backend article
     */
    @ApiOperation(value = "根据id查看后台文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @GetMapping("/admin/articles/{articleId}")
    public Result<ArticleVO> getArticleBackById(@PathVariable("articleId") Integer articleId) {
        return Result.ok(articleService.getArticleBackById(articleId));
    }

    /**
     * search article through id
     *
     * @param articleId article id
     * @return {@link Result< ArticleDTO >} article info
     */
    @ApiOperation(value = "根据id查看文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @GetMapping("/articles/{articleId}")
    public Result<ArticleDTO> getArticleById(@PathVariable("articleId") Integer articleId) {
        return Result.ok(articleService.getArticleById(articleId));
    }

    /**
     * search article through condition
     *
     * @param condition condition
     * @return {@link Result< ArticlePreviewListDTO >} article list
     */
    @ApiOperation(value = "根据条件查询文章")
    @GetMapping("/articles/condition")
    public Result<ArticlePreviewListDTO> listArticlesByCondition(ConditionVO condition) {
        return Result.ok(articleService.listArticlesByCondition(condition));
    }

    /**
     * search article
     *
     * @param condition condition
     * @return {@link Result< ArticleSearchDTO >} article list
     */
    @ApiOperation(value = "搜索文章")
    @GetMapping("/articles/search")
    public Result<List<ArticleSearchDTO>> listArticlesBySearch(ConditionVO condition) {
        return Result.ok(articleService.listArticlesBySearch(condition));
    }

    /**
     * like article
     *
     * @param articleId article id
     * @return {@link Result<>}
     */
    @ApiOperation(value = "点赞文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @PostMapping("/articles/{articleId}/like")
    public Result<?> saveArticleLike(@PathVariable("articleId") Integer articleId) {
        articleService.saveArticleLike(articleId);
        return Result.ok();
    }

    /**
     * upload article to hdfs
     *
     * @param articleIdList article id list
     * @return {@link List<String>} article urllist
     */
    @ApiOperation(value = "上传文章到hdfs")
    @ApiImplicitParam(name = "articleIdList", value = "文章id", required = true, dataType = "List<Integer>")
    @PostMapping("/admin/articles/export")
    public Result<List<String>> exportArticles(@RequestBody List<Integer> articleIdList) {
        return Result.ok(articleService.exportArticles(articleIdList));
    }

    /**
     * import article
     *
     * @param file article
     * @param type article type
     * @return {@link Result<>}
     */
    @ApiOperation(value = "导入文章")
    @PostMapping("/admin/articles/import")
    public Result<?> importArticles(MultipartFile file, @RequestParam(required = false) String type) {
        articleImportStrategyContext.importArticles(file, type);
        return Result.ok();
    }

    /**
     * hot articles
     *
     * @return {@link List<ArticleRankDTO>} hot 5 articles
     */
    @ApiOperation(value = "热榜文章")
    @GetMapping ("/articles/rank")
    public Result<List<ArticleRankDTO>> articlesRank() {
        return Result.ok(articleService.articlesRank());
    }

    /**
     * follow articles
     *
     * @return {@link List<ArchiveDTO>}
     */
    @ApiOperation(value = "关注文章")
    @GetMapping ("/articles/follow")
    public Result<PageResult<ArchiveDTO>> articlesFollow(@RequestParam Integer size, @RequestParam Integer current) {
        return Result.ok(articleService.articlesFollow(size, current));
    }

    /**
     * recommend articles
     *
     * @return {@link List<ArchiveDTO>}
     */
    @ApiOperation(value = "推荐文章")
    @GetMapping ("/articles/recommend")
    public Result<Set<ArchiveDTO>> articlesRecommend() {
        return Result.ok(articleService.articlesRecommend());
    }

    /**
     * composer
     *
     * @return {@link Result <ComposeHomeDTO>} composer home
     */
    @ApiOperation(value = "查看创作首页")
    @GetMapping("/compose/home")
    public Result<ComposeHomeDTO> composeHome() {
        return Result.ok(articleService.composeHome());
    }

}
