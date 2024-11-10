package com.example.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.training.dto.ArticleRankDTO;
import com.example.training.dto.FileCollectionBackDTO;
import com.example.training.dto.FileCollectionDTO;
import com.example.training.entity.FileCollection;
import com.example.training.vo.*;

import java.util.List;

/**
 * @author Xuxinyuan
 */
public interface FileCollectionService extends IService<FileCollection> {
    /**
     * 保存或更新文档合集
     *
     * @param fileCollectionVO 文档合集信息
     */
    void saveOrUpdateFileCollection(FileCollectionVO fileCollectionVO);

    /**
     * 查看后台文档合集列表
     *
     * @param condition 条件
     * @return {@link Result <FileCollectionBackDTO>} 文档合集列表
     */
    PageResult<FileCollectionBackDTO> listFileCollectionBacks(ConditionVO condition);

    /**
     * 获取后台文档合集列表信息
     *
     * @return {@link Result<FileCollectionDTO>} 文档合集列表信息
     */
    List<FileCollectionDTO> listFileCollectionBackInfos(int type);

    /**
     * 根据id获取后台文档合集信息
     *
     * @param collectionId 文档合集id
     * @return {@link Result}文档合集信息
     */
    FileCollectionBackDTO getFileCollectionBackById(Integer collectionId);

    /**
     * 根据id删除文档合集
     *
     * @param collectionId 文档合集id
     */
    void deleteFileCollectionById(Integer collectionId);

    /**
     * 获取文档合集列表
     *
     * @param condition 条件
     * @return {@link Result<FileCollectionDTO>} 文档合集列表
     */
    PageResult<FileCollectionDTO> listFileCollections(ConditionVO condition);

    /**
     * 热榜文档合集
     *
     * @return {@link List< ArticleRankDTO >} 热榜前五的合集
     */
    List<FileCollectionDTO> collectionsRank();

    /**
     * 上次文档合集
     *
     * @return {@link FileCollectionDTO} 上次文档合集
     */
    List<FileCollectionDTO> collectionLast();

    /**
     * 修改用户文档权限
     *
     * @param userCollectionVO 用户文档信息
     */
    void updateUserCollection(UserCollectionVO userCollectionVO);
}
