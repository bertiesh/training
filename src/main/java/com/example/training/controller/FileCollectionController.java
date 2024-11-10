package com.example.training.controller;

import com.example.training.annotation.OptLog;
import com.example.training.dto.*;
import com.example.training.entity.FileCollection;
import com.example.training.enums.FilePathEnum;
import com.example.training.service.FileCollectionService;
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

import static com.baomidou.mybatisplus.core.assist.ISqlRunner.UPDATE;
import static com.example.training.constant.OptTypeConst.REMOVE;
import static com.example.training.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * @author Xinyuan Xu
 */
@Api(tags = "文集模块")
@RestController
public class FileCollectionController {
    @Autowired
    private UploadStrategyContext uploadStrategyContext;
    @Autowired
    private FileCollectionService fileCollectionService;

    /**
     * upload collection cover
     *
     * @param file img
     * @return {@link Result <String>} collection cover address
     */
    @ApiOperation(value = "上传文档合集封面")
    @ApiImplicitParam(name = "file", value = "文档合集封面", required = true, dataType = "MultipartFile[]")
    @PostMapping("/admin/files/collections/cover")
    public Result<List<String>> saveFileCollectionCover(MultipartFile[] file) {
        return Result.ok(uploadStrategyContext.executeUploadMultipleStrategy(file, FilePathEnum.FILE.getPath()));
    }

    /**
     * add/update article collection
     *
     * @param fileCollectionVO file collection info
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新文档合集")
    @PostMapping("/admin/files/collections")
    public Result<?> saveOrUpdateFileCollection(@Valid @RequestBody FileCollectionVO fileCollectionVO) {
        fileCollectionService.saveOrUpdateFileCollection(fileCollectionVO);
        return Result.ok();
    }

    /**
     * search backend article collection
     *
     * @param condition condition
     * @return {@link Result<FileCollectionBackDTO>} article collection list
     */
    @ApiOperation(value = "查看后台文档合集列表")
    @GetMapping("/admin/files/collections")
    public Result<PageResult<FileCollectionBackDTO>> listFileCollectionBacks(ConditionVO condition) {
        return Result.ok(fileCollectionService.listFileCollectionBacks(condition));
    }

    /**
     * get backend article collection info
     *
     * @return {@link Result<FileCollectionDTO>} article collection
     */
    @ApiOperation(value = "获取后台文档合集列表信息")
    @GetMapping("/admin/files/collections/info")
    public Result<List<FileCollectionDTO>> listFileCollectionBackInfos(@RequestParam int type) {
        return Result.ok(fileCollectionService.listFileCollectionBackInfos(type));
    }

    /**
     * get backend article collection info by id
     *
     * @param collectionId collection id
     * @return {@link Result} article collection
     */
    @ApiOperation(value = "根据id获取后台文档合集信息")
    @ApiImplicitParam(name = "collectionId", value = "文档合集id", required = true, dataType = "Integer")
    @GetMapping("/admin/files/collections/{collectionId}/info")
    public Result<FileCollectionBackDTO> getFileCollectionBackById(@PathVariable("collectionId") Integer collectionId) {
        return Result.ok(fileCollectionService.getFileCollectionBackById(collectionId));
    }

    /**
     * delete article collection by id
     *
     * @param collectionId collection id
     * @return {@link Result}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "根据id删除文档合集")
    @ApiImplicitParam(name = "collectionId", value = "文档合集id", required = true, dataType = "Integer")
    @DeleteMapping("/admin/files/collections/{collectionId}")
    public Result<?> deleteFileCollectionById(@PathVariable("collectionId") Integer collectionId) {
        fileCollectionService.deleteFileCollectionById(collectionId);
        return Result.ok();
    }

    /**
     * get article collection
     *
     * @param condition condition
     * @return {@link Result<FileCollectionDTO>} collection list
     */
    @ApiOperation(value = "获取文档合集列表")
    @GetMapping("/files/collections")
    public Result<PageResult<FileCollectionDTO>> listFileCollections(ConditionVO condition) {
        return Result.ok(fileCollectionService.listFileCollections(condition));
    }

    /**
     * hot article collection
     *
     * @return {@link List<ArticleRankDTO>} hot 5 collection
     */
    @ApiOperation(value = "热榜合集")
    @GetMapping ("/files/collections/rank")
    public Result<List<FileCollectionDTO>> collectionsRank() {
        return Result.ok(fileCollectionService.collectionsRank());
    }

    /**
     * last file collection
     *
     * @return {@link FileCollectionDTO} last file collection
     */
    @ApiOperation(value = "上次文档合集")
    @GetMapping ("/files/collections/last")
    public Result<List<FileCollectionDTO>> collectionLast() {
        return Result.ok(fileCollectionService.collectionLast());
    }

    /**
     * update user file permission
     *
     * @param userCollectionVO user file info
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改用户文档权限")
    @PutMapping("/users/collection")
    public Result<?> updateUserCollection(@Valid @RequestBody UserCollectionVO userCollectionVO) {
        fileCollectionService.updateUserCollection(userCollectionVO);
        return Result.ok();
    }
}
