package com.example.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.training.dto.FileBackDTO;
import com.example.training.dto.FileDTO;
import com.example.training.entity.File;
import com.example.training.vo.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Xuxinyuan
 */
public interface FileService extends IService<File> {
    /**
     * 获取后台文档列表
     *
     * @param condition 条件
     * @return {@link Result <FileBackDTO>} 文档列表
     */
    PageResult<FileBackDTO> listFiles(ConditionVO condition);

    /**
     * 更新文档信息
     *
     * @param fileInfoVO 文档信息
     */
    void updateFile(FileInfoVO fileInfoVO);

    /**
     * 保存文档
     *
     * @param fileVO 文档
     */
    void saveFiles(FileVO fileVO);

    /**
     * 移动文档文档合集
     *
     * @param fileVO 文档信息
     */
    void updateFilesCollection(FileVO fileVO);

    /**
     * 更新文档删除状态
     *
     * @param deleteVO 删除信息
     */
    void updateFileDelete(DeleteVO deleteVO);

    /**
     * 删除文档
     *
     * @param fileIdList 文档id列表
     */
    void deleteFiles(List<Integer> fileIdList);

    /**
     * 根据文档合集id查看文档列表
     *
     * @param collectionId 文档合集id
     * @param current
     * @param size
     * @return {@link Result< FileDTO >} 文档列表
     */
    FileDTO listFilesByCollectionId(Integer collectionId, Integer current, Integer size);

    /**
     * 根据文档地址获取文档
     *
     * @param filePath 文档路径
     * @param response http响应
     * @param request
     * @throws IOException IO异常
     */
    void displayFiles(String filePath, HttpServletResponse response, HttpServletRequest request) throws IOException;

    /**
     * 看过文档
     *
     * @param fileId 文档id
     */
    void saveFileHistory(Integer fileId);
}
