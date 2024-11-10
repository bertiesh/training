package com.example.training.controller;

import com.example.training.annotation.OptLog;
import com.example.training.dto.PhotoBackDTO;
import com.example.training.dto.PhotoDTO;
import com.example.training.service.PhotoService;
import com.example.training.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.example.training.constant.OptTypeConst.*;

/**
 * @author Xuxinyuan
 */
@Api(tags = "照片模块")
@RestController
public class PhotoController {
    @Autowired
    private PhotoService photoService;

    /**
     * get backend photo
     *
     * @param condition condition
     * @return {@link Result <PhotoBackDTO>} photo list
     */
    @ApiOperation(value = "根据相册id获取照片列表")
    @GetMapping("/admin/photos")
    public Result<PageResult<PhotoBackDTO>> listPhotos(ConditionVO condition) {
        return Result.ok(photoService.listPhotos(condition));
    }

    /**
     * update photo info
     *
     * @param photoInfoVO photo info
     * @return {@link Result}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "更新照片信息")
    @PutMapping("/admin/photos")
    public Result<?> updatePhoto(@Valid @RequestBody PhotoInfoVO photoInfoVO) {
        photoService.updatePhoto(photoInfoVO);
        return Result.ok();
    }

    /**
     * save photo
     *
     * @param photoVO photo
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE)
    @ApiOperation(value = "保存照片")
    @PostMapping("/admin/photos")
    public Result<?> savePhotos(@Valid @RequestBody PhotoVO photoVO) {
        photoService.savePhotos(photoVO);
        return Result.ok();
    }

    /**
     * move photo
     *
     * @param photoVO photo info
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "移动照片相册")
    @PutMapping("/admin/photos/album")
    public Result<?> updatePhotosAlbum(@Valid @RequestBody PhotoVO photoVO) {
        photoService.updatePhotosAlbum(photoVO);
        return Result.ok();
    }

    /**
     * update photo deletion
     *
     * @param deleteVO deletion info
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "更新照片删除状态")
    @PutMapping("/admin/photos/delete")
    public Result<?> updatePhotoDelete(@Valid @RequestBody DeleteVO deleteVO) {
        photoService.updatePhotoDelete(deleteVO);
        return Result.ok();
    }

    /**
     * delete photo
     *
     * @param photoIdList photo id list
     * @return {@link Result<>}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除照片")
    @DeleteMapping("/admin/photos")
    public Result<?> deletePhotos(@RequestBody List<Integer> photoIdList) {
        photoService.deletePhotos(photoIdList);
        return Result.ok();
    }

    /**
     * get photo list by album id
     *
     * @param albumId album id
     * @return {@link Result<PhotoDTO>} photo list
     */
    @ApiOperation(value = "根据相册id查看照片列表")
    @GetMapping("/albums/{albumId}/photos")
    public Result<PhotoDTO> listPhotosByAlbumId(@PathVariable("albumId") Integer albumId) {
        return Result.ok(photoService.listPhotosByAlbumId(albumId));
    }

    /**
     * get img by address
     *
     * @param photoPath photo path
     * @param response http response
     */
    @ApiOperation(value = "根据图片地址获取图像", httpMethod = "GET")
    @RequestMapping("/photos/display")
    public void listPhotosByAddress(@RequestParam String photoPath, HttpServletResponse response) throws IOException {
        photoService.displayPhotos(photoPath, response);
    }
}
