package com.example.training.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.constant.CommonConst;
import com.example.training.constant.RedisPrefixConst;
import com.example.training.dao.FileCollectionDao;
import com.example.training.dao.FileDao;
import com.example.training.dao.LastFileCollectionDao;
import com.example.training.dao.UserCollectionDao;
import com.example.training.dto.*;
import com.example.training.entity.File;
import com.example.training.entity.FileCollection;
import com.example.training.entity.LastFileCollection;
import com.example.training.entity.UserCollection;
import com.example.training.exception.BizException;
import com.example.training.service.*;
import com.example.training.util.BeanCopyUtils;
import com.example.training.util.CommonUtils;
import com.example.training.util.PageUtils;
import com.example.training.util.UserUtils;
import com.example.training.vo.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.ibatis.annotations.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.training.enums.ArticleStatusEnum.PUBLIC;

/**
 * @author Xuxinyuan
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileDao, File> implements FileService {
    @Autowired
    private FileDao fileDao;
    @Autowired
    private FileCollectionDao fileCollectionDao;
    @Autowired
    private LastFileCollectionDao lastFileCollectionDao;
    @Autowired
    private UserCollectionDao userCollectionDao;
    @Autowired
    private FileCollectionService fileCollectionService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private LastFileCollectionService lastFileCollectionService;
    @Autowired
    private HttpSession session;
    @Value("${upload.hdfs.url}")
    private String hdfsUrl;

    @Override
    public PageResult<FileBackDTO> listFiles(ConditionVO condition) {
        // 查询文档列表
        Page<File> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        Page<File> filePage = fileDao.selectPage(page, new LambdaQueryWrapper<File>()
                .eq(Objects.nonNull(condition.getAlbumId()), File::getCollectionId, condition.getAlbumId())
                .eq(Objects.nonNull(condition.getIsDelete()), File::getIsDelete, condition.getIsDelete())
                .eq(Objects.nonNull(condition.getType()), File::getType, condition.getType())
                .orderByDesc(File::getId)
                .orderByDesc(File::getUpdateTime));
        List<FileBackDTO> fileList = BeanCopyUtils.copyList(filePage.getRecords(), FileBackDTO.class);
        return new PageResult<>(fileList, (int) filePage.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateFile(FileInfoVO fileInfoVO) {
        File file = BeanCopyUtils.copyObject(fileInfoVO, File.class);
        fileDao.updateById(file);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveFiles(FileVO fileVO) {
        List<File> fileList = fileVO.getFileUrlList().stream().map(item -> File.builder()
                        .collectionId(fileVO.getCollectionId())
                        .fileName(IdWorker.getIdStr())
                        .fileSrc(item)
                        .type(fileCollectionDao.selectById(fileVO.getCollectionId()).getType())
                        .build())
                .collect(Collectors.toList());
        this.saveBatch(fileList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateFilesCollection(FileVO fileVO) {
        List<File> fileList = fileVO.getFileIdList().stream().map(item -> File.builder()
                        .id(item)
                        .collectionId(fileVO.getCollectionId())
                        .build())
                .collect(Collectors.toList());
        this.updateBatchById(fileList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateFileDelete(DeleteVO deleteVO) {
        // 更新文档状态
        List<File> fileList = deleteVO.getIdList().stream().map(item -> File.builder()
                        .id(item)
                        .isDelete(deleteVO.getIsDelete())
                        .build())
                .collect(Collectors.toList());
        this.updateBatchById(fileList);
        // 若恢复文档所在的文档合集已删除，恢复文档合集
        if (deleteVO.getIsDelete().equals(CommonConst.FALSE)) {
            List<FileCollection> fileCollectionList = fileDao.selectList(new LambdaQueryWrapper<File>()
                            .select(File::getCollectionId)
                            .in(File::getId, deleteVO.getIdList())
                            .groupBy(File::getCollectionId))
                    .stream()
                    .map(item -> FileCollection.builder()
                            .id(item.getCollectionId())
                            .isDelete(CommonConst.FALSE)
                            .build())
                    .collect(Collectors.toList());
            fileCollectionService.updateBatchById(fileCollectionList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteFiles(List<Integer> fileIdList) {
        fileDao.deleteBatchIds(fileIdList);
    }

    @Override
    public FileDTO listFilesByCollectionId(Integer collectionId, Integer current, Integer size) {
        // 查询文档合集信息
        FileCollection fileCollection = fileCollectionService.getOne(new LambdaQueryWrapper<FileCollection>()
                .eq(FileCollection::getId, collectionId)
                .eq(FileCollection::getIsDelete, CommonConst.FALSE));
        if (Objects.isNull(fileCollection)) {
            throw new BizException("文档合集不存在");
        }
        int notPurchased = 0;
        if (fileCollection.getStatus() > 1) {
            UserCollection userCollection = userCollectionDao.selectOne(new LambdaQueryWrapper<UserCollection>()
                    .eq(UserCollection::getUserInfoId, UserUtils.getLoginUser().getUserInfoId())
                    .eq(UserCollection::getCollectionId, collectionId));
            if (Objects.isNull(userCollection)) {
                notPurchased = 1;
            }
        }
        // 查询文档列表
        int count = Math.toIntExact(fileDao.selectCount(new LambdaQueryWrapper<File>().eq(File::getCollectionId, collectionId)
                .eq(File::getIsDelete, CommonConst.FALSE)));
        Page<File> page = new Page<>(current, size);
        List<String> fileList = fileDao.selectPage(page, new LambdaQueryWrapper<File>()
                        .select(File::getFileSrc)
                        .eq(File::getCollectionId, collectionId)
                        .eq(File::getIsDelete, CommonConst.FALSE)
                        .orderByDesc(File::getId))
                .getRecords()
                .stream()
                .map(File::getFileSrc)
                .collect(Collectors.toList());
        // 查询看过
        List<File> files = fileDao.selectPage(page, new LambdaQueryWrapper<File>().eq(File::getCollectionId, collectionId)
                        .eq(File::getIsDelete, CommonConst.FALSE).orderByDesc(File::getId)).getRecords();
        List<FileInfoDTO> fileInfoDTOList = new ArrayList<>();
        for (File file : files) {
            fileInfoDTOList.add(BeanCopyUtils.copyObject(file, FileInfoDTO.class));
        }
        String historyKey, historyCount;
        if (fileCollection.getType() == 1) {
            historyKey = RedisPrefixConst.DOCUMENT_USER_HISTORY + UserUtils.getLoginUser().getUserInfoId();
            historyCount = RedisPrefixConst.DOCUMENT_HISTORY_COUNT;
        }else {
            historyKey = RedisPrefixConst.MOVIE_USER_HISTORY + UserUtils.getLoginUser().getUserInfoId();
            historyCount = RedisPrefixConst.MOVIE_HISTORY_COUNT;
        }
        Map<String, Object> historyCountMap = redisService.hGetAll(historyCount);
        fileInfoDTOList.forEach(item -> {
            item.setHistoryCount((Integer) historyCountMap.get(item.getId().toString()));
            item.setIsHistory(redisService.sIsMember(historyKey, item.getId()));
        });

        // 更新文档合集浏览量
        updateCollectionViewsCount(collectionId);

        Date now = new Date();
        List<FileCollectionDTO> fileRecommendInfoDTOList = BeanCopyUtils.copyList(fileCollectionDao
                .selectList(new LambdaQueryWrapper<FileCollection>()
                .eq(FileCollection::getIsDelete, CommonConst.FALSE).orderByDesc(FileCollection::getId)
                .between(FileCollection::getCreateTime, DateUtil.offsetDay(now, -7), now)), FileCollectionDTO.class);
        while (fileRecommendInfoDTOList.size() > 3) {
            fileRecommendInfoDTOList.remove(fileRecommendInfoDTOList.size() - 1);
        }
        List<FileCollectionDTO> filesAdd = BeanCopyUtils.copyList(fileCollectionDao.selectList(new LambdaQueryWrapper<FileCollection>()
                .eq(FileCollection::getIsDelete, CommonConst.FALSE)
                .notBetween(FileCollection::getCreateTime, DateUtil.offsetDay(now, -7), now)), FileCollectionDTO.class);
        Random random = new Random();
        while (fileRecommendInfoDTOList.size() < 3) {
            int index = random.nextInt(filesAdd.size());
            fileRecommendInfoDTOList.add(filesAdd.get(index));
            filesAdd.remove(index);
        }

        LastFileCollection lastFileCollection = new LastFileCollection();
        lastFileCollection.setCollectionId(collectionId);
        lastFileCollection.setUserId(UserUtils.getLoginUser().getUserInfoId());
        if (lastFileCollectionDao.selectCount(new LambdaQueryWrapper<LastFileCollection>()
                .eq(LastFileCollection::getUserId, lastFileCollection.getUserId())
                .eq(LastFileCollection::getCollectionId, lastFileCollection.getCollectionId())) > 0) {
            lastFileCollection.setId(lastFileCollectionDao.selectOne(new LambdaQueryWrapper<LastFileCollection>()
                    .eq(LastFileCollection::getUserId, lastFileCollection.getUserId())
                    .eq(LastFileCollection::getCollectionId, lastFileCollection.getCollectionId())).getId());
        } else if (lastFileCollectionDao.selectCount(new LambdaQueryWrapper<LastFileCollection>()
                .eq(LastFileCollection::getUserId, lastFileCollection.getUserId())) > 4) {
            lastFileCollectionService.removeById(lastFileCollectionDao.selectList(
                            new LambdaQueryWrapper<LastFileCollection>()
                                    .eq(LastFileCollection::getUserId, lastFileCollection.getUserId())
                                    .last("ORDER BY COALESCE(update_time, create_time) ASC"))
                    .get(0).getId());
        }
        lastFileCollectionService.saveOrUpdate(lastFileCollection);

        Map<Object, Double> viewsCountMap = redisService.zAllScore(RedisPrefixConst.COLLECTION_VIEWS_COUNT);

        List<Integer> list = CommonUtils.castList(JSON.parseObject(fileCollection.getProjectIds(), List.class), Integer.class);
        if (list.size() == 0) {
            list = null;
        }
        return FileDTO.builder()
                .fileCollectionCover(fileCollection.getCollectionCover())
                .fileCollectionName(fileCollection.getCollectionName())
                .collectionDesc(fileCollection.getCollectionDesc())
                .projectIdList(list)
                .fileList(fileList)
                .type(fileCollection.getType())
                .count(count)
                .notPurchased(notPurchased)
                .viewsCount(viewsCountMap.get(collectionId).intValue())
                .fileInfoList(fileInfoDTOList)
                .fileRecommendInfoList(fileRecommendInfoDTOList)
                .build();
    }

    private void updateCollectionViewsCount(Integer collectionId) {
        // 判断是否第一次访问，增加浏览量
        Set<Integer> collectionSet = CommonUtils.castSet(Optional.ofNullable(session.getAttribute(CommonConst.COLLECTION_SET)).orElseGet(HashSet::new), Integer.class);
        if (!collectionSet.contains(collectionId)) {
            collectionSet.add(collectionId);
            session.setAttribute(CommonConst.COLLECTION_SET, collectionSet);
            // 浏览量+1
            redisService.zIncr(RedisPrefixConst.COLLECTION_VIEWS_COUNT, collectionId, 1D);
        }
    }

    @Override
    public void displayFiles(String filePath, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String contentType = "";
        if (filePath == null) {
            response.sendError(500, "fpath不能为空");
        }
        switch (Objects.requireNonNull(filePath).substring(filePath.lastIndexOf('.') + 1)){
            case "txt":
                contentType = "text/plain";
                break;
            case "docx":
                contentType = "application/msword";
                break;
            case "pdf":
                contentType = "application/pdf";
                break;
            case "xls":
            case "xlsx":
                contentType = "application/x-xls";
                break;
            case "avi":
                contentType = "video/x-msvideo";
                break;
            case "mpg":
            case "mpeg":
            case "mpe":
                contentType = "video/mpeg";
                break;
            case "m4v":
                contentType = "video/x-m4v";
                break;
            case "mov":
            case "qt":
                contentType = "video/quicktime";
                break;
            case "mp4":
                contentType = "video/mp4";
                break;
            case "ogv":
                contentType = "video/ogv";
                break;
                default :
                    response.sendError(500, "文件类型不合理");
        }
        Configuration config = new Configuration();
        config.set("fs.defaultFS", hdfsUrl);
        FileSystem fs = new Path(filePath.substring(filePath.lastIndexOf("/") + 1)).getFileSystem(config);
        FSDataInputStream in = fs.open(new Path(filePath));

        final long fileLen = fs.getFileStatus(new Path(filePath)).getLen();
        response.setHeader("Content-type",contentType);
        OutputStream out = response.getOutputStream();
        String filename = filePath.substring(filePath.lastIndexOf("/") + 1);
        response.setHeader("Content-Disposition", "attachment; filename = " + filename);
        response.setContentType(contentType);
        response.setContentLength((int)fileLen);
        if ("video/mp4".equals(contentType) && request.getHeader("Range") != null) {
            String range = request.getHeader("Range");
            in.seek(Long.parseLong(range.substring(6, range.indexOf("-"))));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Range", range.replaceAll("=", " ") + "/" + fileLen);
            response.setContentLength((int)fileLen - Integer.parseInt(range.substring(6, range.indexOf("-"))));
        }
        IOUtils.copyBytes(in, out, fileLen, false);
        in.close();
        out.close();
    }

    @Override
    public void saveFileHistory(Integer fileId) {
        //判断文档类型
        String prefix, count;
        if (fileDao.selectById(fileId).getType() == 1){
            prefix = RedisPrefixConst.DOCUMENT_USER_HISTORY;
            count = RedisPrefixConst.DOCUMENT_HISTORY_COUNT;
        }else {
            prefix = RedisPrefixConst.MOVIE_USER_HISTORY;
            count = RedisPrefixConst.MOVIE_HISTORY_COUNT;
        }
        setRedis(fileId, prefix, count);
    }

    private void setRedis(Integer fileId, String prefix, String count) {
        //判断有没有
        String fileKey = prefix + UserUtils.getLoginUser().getUserInfoId();
        if (!redisService.sIsMember(fileKey, fileId)) {
            // 没有则增加文档id
            redisService.sAdd(fileKey, fileId);
            // 文档redis量+1
            redisService.hIncr(count, fileId.toString(), 1L);
        }
    }
}
