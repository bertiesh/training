package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.dto.FileCollectionBackDTO;
import com.example.training.dto.PhotoAlbumBackDTO;
import com.example.training.entity.FileCollection;
import com.example.training.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface FileCollectionDao extends BaseMapper<FileCollection> {
    /**
     * get backend file collection
     *
     * @param current   page
     * @param size      size
     * @param condition condition
     * @return {@link List < FileCollectionBackDTO >} file collection
     */
    List<FileCollectionBackDTO> listFileCollectionBacks(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);
}
