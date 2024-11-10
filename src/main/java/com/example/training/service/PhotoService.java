package com.example.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.training.dto.PhotoBackDTO;
import com.example.training.dto.PhotoDTO;
import com.example.training.entity.Photo;
import com.example.training.vo.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Xuxinyuan
 */
public interface PhotoService extends IService<Photo> {
    /**
     * 根据相册id获取照片列表
     *
     * @param condition 条件
     * @return {@link PageResult <PhotoBackDTO>} 照片列表
     */
    PageResult<PhotoBackDTO> listPhotos(ConditionVO condition);

    /**
     * 更新照片信息
     *
     * @param photoInfoVO 照片信息
     */
    void updatePhoto(PhotoInfoVO photoInfoVO);

    /**
     * 保存照片
     *
     * @param photoVO 照片
     */
    void savePhotos(PhotoVO photoVO);

    /**
     * 移动照片相册
     *
     * @param photoVO 照片信息
     */
    void updatePhotosAlbum(PhotoVO photoVO);

    /**
     * 更新照片删除状态
     *
     * @param deleteVO 删除信息
     */
    void updatePhotoDelete(DeleteVO deleteVO);

    /**
     * 删除照片
     *
     * @param photoIdList 照片id列表
     */
    void deletePhotos(List<Integer> photoIdList);

    /**
     * 根据相册id查看照片列表
     *
     * @param albumId 相册id
     * @return {@link List<PhotoDTO>} 照片列表
     */
    PhotoDTO listPhotosByAlbumId(Integer albumId);

    /**
     * 根据图片地址获取图像
     *
     * @param photoPath    相册id
     * @param response http响应
     */
    void displayPhotos(String photoPath, HttpServletResponse response) throws IOException;
}
