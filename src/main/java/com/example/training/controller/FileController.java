package com.example.training.controller;

import com.example.training.annotation.OptLog;
import com.example.training.dto.FileBackDTO;
import com.example.training.dto.FileDTO;
import com.example.training.dto.PhotoBackDTO;
import com.example.training.dto.PhotoDTO;
import com.example.training.enums.FilePathEnum;
import com.example.training.service.FileService;
import com.example.training.strategy.context.UploadStrategyContext;
import com.example.training.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.example.training.constant.OptTypeConst.*;

/**
 * @author Xinyuan Xu
 */
@Api(tags = "文档模块")
@RestController
public class FileController {
    @Autowired
    private FileService fileService;

    /**
     * get background file list
     *
     * @param condition condition
     * @return {@link Result <FileBackDTO>} file list
     */
    @ApiOperation(value = "根据文档合集id获取文档列表")
    @GetMapping("/admin/files")
    public Result<PageResult<FileBackDTO>> listFiles(ConditionVO condition) {
        return Result.ok(fileService.listFiles(condition));
    }

    /**
     * update file info
     *
     * @param fileInfoVO file info
     * @return {@link Result}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "更新文档信息")
    @PutMapping("/admin/files")
    public Result<?> updateFile(@Valid @RequestBody FileInfoVO fileInfoVO) {
        fileService.updateFile(fileInfoVO);
        return Result.ok();
    }

    /**
     * save file
     *
     * @param fileVO file
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE)
    @ApiOperation(value = "保存文档")
    @PostMapping("/admin/files")
    public Result<?> saveFiles(@Valid @RequestBody FileVO fileVO) {
        fileService.saveFiles(fileVO);
        return Result.ok();
    }

    /**
     * move file
     *
     * @param fileVO file
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "移动文档文档合集")
    @PutMapping("/admin/files/collection")
    public Result<?> updateFilesCollection(@Valid @RequestBody FileVO fileVO) {
        fileService.updateFilesCollection(fileVO);
        return Result.ok();
    }

    /**
     * update file deletion
     *
     * @param deleteVO file deletion
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "更新文档删除状态")
    @PutMapping("/admin/files/delete")
    public Result<?> updateFileDelete(@Valid @RequestBody DeleteVO deleteVO) {
        fileService.updateFileDelete(deleteVO);
        return Result.ok();
    }

    /**
     * delete file
     *
     * @param fileIdList file id list
     * @return {@link Result<>}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除文档")
    @DeleteMapping("/admin/files")
    public Result<?> deleteFiles(@RequestBody List<Integer> fileIdList) {
        fileService.deleteFiles(fileIdList);
        return Result.ok();
    }

    /**
     * search file by collection id
     *
     * @param collectionId collection id
     * @return {@link Result<FileDTO>} file list
     */
    @ApiOperation(value = "根据文档id查看文档列表")
    @GetMapping("/collections/{collectionId}/files")
    public Result<FileDTO> listFilesByCollectionId(@PathVariable("collectionId") Integer collectionId,
                                                   @RequestParam(required = false, defaultValue = "1") Integer current,
                                                   @RequestParam(required = false, defaultValue = "10") Integer size) {
        return Result.ok(fileService.listFilesByCollectionId(collectionId, current, size));
    }

    /**
     * get file by address
     *
     * @param filePath file path
     * @param response http response
     */
    @ApiOperation(value = "根据文档地址获取文档", httpMethod = "GET")
    @RequestMapping("/files/display")
    public void listFilesByAddress(@RequestParam String filePath, HttpServletRequest request, HttpServletResponse response) throws IOException {
        fileService.displayFiles(filePath, response, request);
    }

    /**
     * view file
     *
     * @param fileId file id
     * @return {@link Result<>}
     */
    @ApiOperation(value = "看过文档")
    @ApiImplicitParam(name = "fileId", value = "文档id", required = true, dataType = "Integer")
    @PostMapping("/files/{fileId}/history")
    public Result<?> saveFileHistory(@PathVariable("fileId") Integer fileId) {
        fileService.saveFileHistory(fileId);
        return Result.ok();
    }
}
