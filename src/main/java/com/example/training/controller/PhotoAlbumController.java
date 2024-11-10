package com.example.training.controller;

import com.example.training.annotation.OptLog;
import com.example.training.dto.PhotoAlbumBackDTO;
import com.example.training.dto.PhotoAlbumDTO;
import com.example.training.enums.FilePathEnum;
import com.example.training.service.PhotoAlbumService;
import com.example.training.strategy.context.UploadStrategyContext;
import com.example.training.vo.ConditionVO;
import com.example.training.vo.PageResult;
import com.example.training.vo.PhotoAlbumVO;
import com.example.training.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static com.example.training.constant.OptTypeConst.REMOVE;
import static com.example.training.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * @author Xinyuan Xu
 */
@Api(tags = "相册模块")
@RestController
public class PhotoAlbumController {
    @Autowired
    private UploadStrategyContext uploadStrategyContext;
    @Autowired
    private PhotoAlbumService photoAlbumService;

    /**
     * upload album cover
     *
     * @param file img
     * @return {@link Result <String>} album cover address
     */
    @ApiOperation(value = "上传相册封面")
    @ApiImplicitParam(name = "file", value = "相册封面", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/photos/albums/cover")
    public Result<String> savePhotoAlbumCover(MultipartFile file) {
        return Result.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.PHOTO.getPath()));
    }

    /**
     * save/update album
     *
     * @param photoAlbumVO album info
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新相册")
    @PostMapping("/admin/photos/albums")
    public Result<?> saveOrUpdatePhotoAlbum(@Valid @RequestBody PhotoAlbumVO photoAlbumVO) {
        photoAlbumService.saveOrUpdatePhotoAlbum(photoAlbumVO);
        return Result.ok();
    }

    /**
     * search backend album
     *
     * @param condition condition
     * @return {@link Result<PhotoAlbumBackDTO>} album list
     */
    @ApiOperation(value = "查看后台相册列表")
    @GetMapping("/admin/photos/albums")
    public Result<PageResult<PhotoAlbumBackDTO>> listPhotoAlbumBacks(ConditionVO condition) {
        return Result.ok(photoAlbumService.listPhotoAlbumBacks(condition));
    }

    /**
     * get backend album info
     *
     * @return {@link Result< PhotoAlbumDTO >} album info list
     */
    @ApiOperation(value = "获取后台相册列表信息")
    @GetMapping("/admin/photos/albums/info")
    public Result<List<PhotoAlbumDTO>> listPhotoAlbumBackInfos() {
        return Result.ok(photoAlbumService.listPhotoAlbumBackInfos());
    }

    /**
     * get backend album info by id
     *
     * @param albumId album id
     * @return {@link Result} album info
     */
    @ApiOperation(value = "根据id获取后台相册信息")
    @ApiImplicitParam(name = "albumId", value = "相册id", required = true, dataType = "Integer")
    @GetMapping("/admin/photos/albums/{albumId}/info")
    public Result<PhotoAlbumBackDTO> getPhotoAlbumBackById(@PathVariable("albumId") Integer albumId) {
        return Result.ok(photoAlbumService.getPhotoAlbumBackById(albumId));
    }

    /**
     * delete album by id
     *
     * @param albumId album id
     * @return {@link Result}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "根据id删除相册")
    @ApiImplicitParam(name = "albumId", value = "相册id", required = true, dataType = "Integer")
    @DeleteMapping("/admin/photos/albums/{albumId}")
    public Result<?> deletePhotoAlbumById(@PathVariable("albumId") Integer albumId) {
        photoAlbumService.deletePhotoAlbumById(albumId);
        return Result.ok();
    }

    /**
     * get album list
     *
     * @param condition condition
     * @return {@link Result<PhotoAlbumDTO>} album list
     */
    @ApiOperation(value = "获取相册列表")
    @GetMapping("/photos/albums")
    public Result<PageResult<PhotoAlbumDTO>> listPhotoAlbums(ConditionVO condition) {
        return Result.ok(photoAlbumService.listPhotoAlbums(condition));
    }
}
