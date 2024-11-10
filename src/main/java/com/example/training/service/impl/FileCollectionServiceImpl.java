package com.example.training.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.constant.CommonConst;
import com.example.training.constant.RedisPrefixConst;
import com.example.training.dao.*;
import com.example.training.dto.FileCollectionBackDTO;
import com.example.training.dto.FileCollectionDTO;
import com.example.training.dto.ProjectBackDTO;
import com.example.training.entity.*;
import com.example.training.exception.BizException;
import com.example.training.service.FileCollectionService;
import com.example.training.service.RedisService;
import com.example.training.service.RewardPointService;
import com.example.training.util.BeanCopyUtils;
import com.example.training.util.CommonUtils;
import com.example.training.util.PageUtils;
import com.example.training.util.UserUtils;
import com.example.training.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Xuxinyuan
 */
@Service
public class FileCollectionServiceImpl extends ServiceImpl<FileCollectionDao, FileCollection> implements FileCollectionService {
    @Autowired
    private FileCollectionDao fileCollectionDao;
    @Autowired
    private FileDao fileDao;
    @Autowired
    private LastFileCollectionDao lastFileCollectionDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private UserCollectionDao userCollectionDao;
    @Autowired
    private RewardPointDao rewardPointDao;
    @Autowired
    private ProjectServiceImpl projectService;
    @Autowired
    private RewardPointService rewardPointService;
    @Autowired
    private RedisService redisService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateFileCollection(FileCollectionVO fileCollectionVO) {
        // 查询文档合集名是否存在
        FileCollection collection = fileCollectionDao.selectOne(new LambdaQueryWrapper<FileCollection>()
                .select(FileCollection::getId)
                .eq(FileCollection::getCollectionName, fileCollectionVO.getCollectionName()));
        if (Objects.nonNull(collection) && !collection.getId().equals(fileCollectionVO.getId())) {
            throw new BizException("文档合集名已存在");
        }
        FileCollection fileCollection = BeanCopyUtils.copyObject(fileCollectionVO, FileCollection.class);
        fileCollection.setProjectIds(JSONUtil.toJsonStr(fileCollectionVO.getProjectIds()));
        this.saveOrUpdate(fileCollection);
    }

    @Override
    public PageResult<FileCollectionBackDTO> listFileCollectionBacks(ConditionVO condition) {
        // 查询文档合集数量
        int count = Math.toIntExact(fileCollectionDao.selectCount(new LambdaQueryWrapper<FileCollection>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), FileCollection::getCollectionName, condition.getKeywords())
                .eq(FileCollection::getIsDelete, CommonConst.FALSE)
                .eq(Objects.nonNull(condition.getType()), FileCollection::getType, condition.getType())));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询文档合集信息
        List<FileCollectionBackDTO> fileCollectionBackList = fileCollectionDao.listFileCollectionBacks(PageUtils.getLimitCurrent(),
                PageUtils.getSize(), condition);
        fileCollectionBackList.forEach(item -> {
            List<Integer> idList = CommonUtils.castList(JSON.parseObject(item.getProjectIds(), List.class), Integer.class);
            if (idList.size() > 0) {
                item.setProjects(BeanCopyUtils.copyList(projectDao.selectBatchIds(idList), ProjectBackDTO.class));
                for (ProjectBackDTO projectBackDTO : item.getProjects()) {
                    projectBackDTO = projectService.getQuestions(projectBackDTO);
                }
            }
        });
        return new PageResult<>(fileCollectionBackList, count);
    }

    @Override
    public List<FileCollectionDTO> listFileCollectionBackInfos(int type) {
        List<FileCollection> fileCollectionList = fileCollectionDao.selectList(new LambdaQueryWrapper<FileCollection>()
                .eq(FileCollection::getIsDelete, CommonConst.FALSE).eq(FileCollection::getType, type));
        return BeanCopyUtils.copyList(fileCollectionList, FileCollectionDTO.class);
    }

    @Override
    public FileCollectionBackDTO getFileCollectionBackById(Integer collectionId) {
        // 查询文档合集信息
        FileCollection fileCollection = fileCollectionDao.selectById(collectionId);
        // 查询文档数量
        Integer fileCount = Math.toIntExact(fileDao.selectCount(new LambdaQueryWrapper<File>()
                .eq(File::getCollectionId, collectionId)
                .eq(File::getIsDelete, CommonConst.FALSE)));
        FileCollectionBackDTO collection = BeanCopyUtils.copyObject(fileCollection, FileCollectionBackDTO.class);
        collection.setFileCount(fileCount);
        List<Integer> idList = CommonUtils.castList(JSON.parseObject(collection.getProjectIds(), List.class), Integer.class);
        if (idList.size() > 0) {
            collection.setProjects(BeanCopyUtils.copyList(projectDao.selectBatchIds(idList), ProjectBackDTO.class));
            for (ProjectBackDTO projectBackDTO : collection.getProjects()) {
                projectBackDTO = projectService.getQuestions(projectBackDTO);
            }
        }
        return collection;
    }

    @Override
    public void deleteFileCollectionById(Integer collectionId) {
        // 查询文档数量
        int count = Math.toIntExact(fileDao.selectCount(new LambdaQueryWrapper<File>()
                .eq(File::getCollectionId, collectionId)));
        if (count > 0) {
            // 若文档合集下存在文档则逻辑删除文档合集和文档
            fileCollectionDao.updateById(FileCollection.builder()
                    .id(collectionId)
                    .isDelete(CommonConst.TRUE)
                    .build());
            fileDao.update(new File(), new LambdaUpdateWrapper<File>()
                    .set(File::getIsDelete, CommonConst.TRUE)
                    .eq(File::getCollectionId, collectionId));
        } else {
            // 若文档合集下不存在文档则直接删除
            fileCollectionDao.deleteById(collectionId);
        }
    }

    @Override
    public PageResult<FileCollectionDTO> listFileCollections(ConditionVO condition) {
        // 查询文档合集列表
        Page<FileCollection> page = new Page<>(condition.getCurrent(), condition.getSize());
        LambdaQueryWrapper<FileCollection> queryWrapper = new LambdaQueryWrapper<FileCollection>()
                .eq(Objects.nonNull(condition.getStatus()), FileCollection::getStatus, condition.getStatus())
                .eq(FileCollection::getIsDelete, CommonConst.FALSE).orderByDesc(FileCollection::getId)
                .eq(Objects.nonNull(condition.getType()), FileCollection::getType, condition.getType())
                .like(StringUtils.isNotBlank(condition.getKeywords()), FileCollection::getCollectionName, condition.getKeywords());
        IPage<FileCollection> page1 = fileCollectionDao.selectPage(page, queryWrapper);
        int count = Math.toIntExact(fileCollectionDao.selectCount(queryWrapper));
        List<FileCollection> fileCollectionList = page1.getRecords();
        List<FileCollectionDTO> fileCollectionDTOList = BeanCopyUtils.copyList(fileCollectionList, FileCollectionDTO.class);
        Map<Object, Double> viewsCountMap = redisService.zAllScore(RedisPrefixConst.COLLECTION_VIEWS_COUNT);
        for (FileCollectionDTO fileCollectionDTO : fileCollectionDTOList) {
            // 查询文档数量
            Integer fileCount = Math.toIntExact(fileDao.selectCount(new LambdaQueryWrapper<File>()
                    .eq(File::getCollectionId, fileCollectionDTO.getId())
                    .eq(File::getIsDelete, CommonConst.FALSE)));
            Double viewsCount = viewsCountMap.get(fileCollectionDTO.getId());
            if (Objects.nonNull(viewsCount)) {
                fileCollectionDTO.setViewsCount(viewsCount.intValue());
            }
            fileCollectionDTO.setFileCount(fileCount);
            fileCollectionDTO.setNotPurchased(0);
            if (fileCollectionDTO.getStatus() > 1) {
                UserCollection userCollection = userCollectionDao.selectOne(new LambdaQueryWrapper<UserCollection>()
                        .eq(UserCollection::getUserInfoId, UserUtils.getLoginUser().getUserInfoId())
                        .eq(UserCollection::getCollectionId, fileCollectionDTO.getId()));
                if (Objects.isNull(userCollection)) {
                    fileCollectionDTO.setNotPurchased(1);
                }
            }
        }
        PageResult<FileCollectionDTO> pageResult = new PageResult<>();
        pageResult.setRecordList(fileCollectionDTOList);
        pageResult.setCount(count);
        return pageResult;
    }

    @Override
    public List<FileCollectionDTO> collectionsRank(){
        Map<Object, Double> collectionMap = redisService.zReverseRangeWithScore(RedisPrefixConst.COLLECTION_VIEWS_COUNT, 0, 5);
        if (CollectionUtils.isNotEmpty(collectionMap)) {
            // 查询合集排行
            return listCollectionRank(collectionMap);
        }
        return null;
    }

    private List<FileCollectionDTO> listCollectionRank(Map<Object, Double> collectionMap) {
        // 提取合集id
        List<Integer> collectionIdList = new ArrayList<>(collectionMap.size());
        collectionMap.forEach((key, value) -> collectionIdList.add((Integer) key));
        // 查询合集信息
        return fileCollectionDao.selectList(new LambdaQueryWrapper<FileCollection>()
                        .select(FileCollection::getId, FileCollection::getCollectionName, FileCollection::getCollectionCover,
                                FileCollection::getCollectionDesc)
                        .eq(FileCollection::getStatus, 1).eq(FileCollection::getIsDelete, false)
                        .in(FileCollection::getId, collectionIdList))
                .stream().map(collection -> FileCollectionDTO.builder()
                        .id(collection.getId())
                        .collectionName(collection.getCollectionName())
                        .collectionCover(collection.getCollectionCover())
                        .collectionDesc(collection.getCollectionDesc())
                        .fileCount(Math.toIntExact(fileDao.selectCount(new LambdaQueryWrapper<File>()
                                .eq(File::getCollectionId, collection.getId())
                                .eq(File::getIsDelete, CommonConst.FALSE))))
                        .viewsCount(collectionMap.get(collection.getId()).intValue())
                        .build())
                .sorted(Comparator.comparingInt(FileCollectionDTO::getViewsCount).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<FileCollectionDTO> collectionLast() {
        List<FileCollection> fileCollections = new ArrayList<>();
        for (LastFileCollection lastFileCollection : lastFileCollectionDao.selectList(new LambdaQueryWrapper<LastFileCollection>()
                .eq(LastFileCollection::getUserId, UserUtils.getLoginUser().getUserInfoId())
                .last("ORDER BY COALESCE(update_time, create_time) DESC"))) {
            fileCollections.add(fileCollectionDao.selectById(lastFileCollection.getCollectionId()));
        }
        return BeanCopyUtils.copyList(fileCollections, FileCollectionDTO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserCollection(UserCollectionVO userCollectionVO) {
        List<UserCollection> userCollection = userCollectionVO.getCollectionIdList().stream()
                .map(collectionId -> UserCollection.builder()
                        .collectionId(collectionId)
                        .userInfoId(UserUtils.getLoginUser().getUserInfoId())
                        .build())
                .collect(Collectors.toList());
        int totalPoints = 0, rewardPoints;
        rewardPoints = rewardPointDao.selectList(new LambdaQueryWrapper<RewardPoint>()
                .eq(RewardPoint::getUserId, UserUtils.getLoginUser().getUserInfoId())
                .orderByDesc(RewardPoint::getCreateTime)).get(0).getTotalPoints();
        for (UserCollection collection : userCollection) {
            userCollectionDao.insert(collection);
            totalPoints += fileCollectionDao.selectById(collection.getCollectionId()).getStatus();
        }
        if (rewardPoints < totalPoints) {
            throw new BizException("积分不足");
        }
        RewardPoint rewardPoint = RewardPoint.builder().userId(UserUtils.getLoginUser().getUserInfoId()).event("购买课程")
                .points(-totalPoints).totalPoints(rewardPoints - totalPoints).build();
        rewardPointService.save(rewardPoint);
    }
}
