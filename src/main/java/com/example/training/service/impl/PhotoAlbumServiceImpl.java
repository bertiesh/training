package com.example.training.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.constant.CommonConst;
import com.example.training.dao.PhotoAlbumDao;
import com.example.training.dao.PhotoDao;
import com.example.training.dto.PhotoAlbumBackDTO;
import com.example.training.dto.PhotoAlbumDTO;
import com.example.training.entity.Photo;
import com.example.training.entity.PhotoAlbum;
import com.example.training.exception.BizException;
import com.example.training.service.PhotoAlbumService;
import com.example.training.util.BeanCopyUtils;
import com.example.training.util.PageUtils;
import com.example.training.vo.ConditionVO;
import com.example.training.vo.PageResult;
import com.example.training.vo.PhotoAlbumVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.example.training.enums.ArticleStatusEnum.PUBLIC;

/**
 * @author Xuxinyuan
 */
@Service
public class PhotoAlbumServiceImpl extends ServiceImpl<PhotoAlbumDao, PhotoAlbum> implements PhotoAlbumService {
    @Autowired
    private PhotoAlbumDao photoAlbumDao;
    @Autowired
    private PhotoDao photoDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdatePhotoAlbum(PhotoAlbumVO photoAlbumVO) {
        // 查询相册名是否存在
        PhotoAlbum album = photoAlbumDao.selectOne(new LambdaQueryWrapper<PhotoAlbum>()
                .select(PhotoAlbum::getId)
                .eq(PhotoAlbum::getAlbumName, photoAlbumVO.getAlbumName()));
        if (Objects.nonNull(album) && !album.getId().equals(photoAlbumVO.getId())) {
            throw new BizException("相册名已存在");
        }
        PhotoAlbum photoAlbum = BeanCopyUtils.copyObject(photoAlbumVO, PhotoAlbum.class);
        this.saveOrUpdate(photoAlbum);
    }

    @Override
    public PageResult<PhotoAlbumBackDTO> listPhotoAlbumBacks(ConditionVO condition) {
        // 查询相册数量
        int count = Math.toIntExact(photoAlbumDao.selectCount(new LambdaQueryWrapper<PhotoAlbum>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), PhotoAlbum::getAlbumName, condition.getKeywords())
                .eq(PhotoAlbum::getIsDelete, CommonConst.FALSE)));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询相册信息
        List<PhotoAlbumBackDTO> photoAlbumBackList = photoAlbumDao.listPhotoAlbumBacks(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        return new PageResult<>(photoAlbumBackList, count);
    }

    @Override
    public List<PhotoAlbumDTO> listPhotoAlbumBackInfos() {
        List<PhotoAlbum> photoAlbumList = photoAlbumDao.selectList(new LambdaQueryWrapper<PhotoAlbum>()
                .eq(PhotoAlbum::getIsDelete, CommonConst.FALSE));
        return BeanCopyUtils.copyList(photoAlbumList, PhotoAlbumDTO.class);
    }

    @Override
    public PhotoAlbumBackDTO getPhotoAlbumBackById(Integer albumId) {
        // 查询相册信息
        PhotoAlbum photoAlbum = photoAlbumDao.selectById(albumId);
        // 查询照片数量
        Integer photoCount = Math.toIntExact(photoDao.selectCount(new LambdaQueryWrapper<Photo>()
                .eq(Photo::getAlbumId, albumId)
                .eq(Photo::getIsDelete, CommonConst.FALSE)));
        PhotoAlbumBackDTO album = BeanCopyUtils.copyObject(photoAlbum, PhotoAlbumBackDTO.class);
        album.setPhotoCount(photoCount);
        return album;
    }

    @Override
    public void deletePhotoAlbumById(Integer albumId) {
        // 查询照片数量
        int count = Math.toIntExact(photoDao.selectCount(new LambdaQueryWrapper<Photo>()
                .eq(Photo::getAlbumId, albumId)));
        if (count > 0) {
            // 若相册下存在照片则逻辑删除相册和照片
            photoAlbumDao.updateById(PhotoAlbum.builder()
                    .id(albumId)
                    .isDelete(CommonConst.TRUE)
                    .build());
            photoDao.update(new Photo(), new LambdaUpdateWrapper<Photo>()
                    .set(Photo::getIsDelete, CommonConst.TRUE)
                    .eq(Photo::getAlbumId, albumId));
        } else {
            // 若相册下不存在照片则直接删除
            photoAlbumDao.deleteById(albumId);
        }
    }

    @Override
    public PageResult<PhotoAlbumDTO> listPhotoAlbums(ConditionVO condition) {
        // 查询相册列表
        Page<PhotoAlbum> page = new Page<>(condition.getCurrent(), condition.getSize());
        IPage<PhotoAlbum> page1 = photoAlbumDao.selectPage(page, new LambdaQueryWrapper<PhotoAlbum>()
                .eq(PhotoAlbum::getStatus, PUBLIC.getStatus()).eq(PhotoAlbum::getIsDelete, CommonConst.FALSE)
                .orderByDesc(PhotoAlbum::getId)
                .like(StringUtils.isNotBlank(condition.getKeywords()), PhotoAlbum::getAlbumName, condition.getKeywords()));
        int count = Math.toIntExact(photoAlbumDao.selectCount(new LambdaQueryWrapper<PhotoAlbum>()
                .eq(PhotoAlbum::getStatus, PUBLIC.getStatus()).eq(PhotoAlbum::getIsDelete, CommonConst.FALSE)
                .orderByDesc(PhotoAlbum::getId)
                .like(StringUtils.isNotBlank(condition.getKeywords()), PhotoAlbum::getAlbumName, condition.getKeywords())));
        List<PhotoAlbum> photoAlbumList = page1.getRecords();
        List<PhotoAlbumDTO> photoAlbumDTOList = BeanCopyUtils.copyList(photoAlbumList, PhotoAlbumDTO.class);
        PageResult<PhotoAlbumDTO> pageResult = new PageResult<>();
        pageResult.setRecordList(photoAlbumDTOList);
        pageResult.setCount(count);
        return pageResult;
    }
}
