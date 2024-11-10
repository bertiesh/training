package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.dto.PhotoAlbumBackDTO;
import com.example.training.entity.PhotoAlbum;
import com.example.training.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface PhotoAlbumDao extends BaseMapper<PhotoAlbum> {
    /**
     * get backend albums
     *
     * @param current   page
     * @param size      size
     * @param condition condition
     * @return {@link List < PhotoAlbumBackDTO >} albums
     */
    List<PhotoAlbumBackDTO> listPhotoAlbumBacks(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

}
