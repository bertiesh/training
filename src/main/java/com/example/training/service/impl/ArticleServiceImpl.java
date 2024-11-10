package com.example.training.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.constant.CommonConst;
import com.example.training.constant.MQPrefixConst;
import com.example.training.constant.RedisPrefixConst;
import com.example.training.dao.*;
import com.example.training.dto.*;
import com.example.training.entity.*;
import com.example.training.enums.FileExtEnum;
import com.example.training.enums.FilePathEnum;
import com.example.training.exception.BizException;
import com.example.training.service.*;
import com.example.training.strategy.context.SearchStrategyContext;
import com.example.training.strategy.context.UploadStrategyContext;
import com.example.training.util.*;
import com.example.training.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.example.training.constant.CommonConst.FALSE;
import static com.example.training.enums.ArticleStatusEnum.DRAFT;
import static com.example.training.enums.ArticleStatusEnum.PUBLIC;
import static com.example.training.enums.CommentTypeEnum.ARTICLE;
import static com.example.training.enums.RewardPointsEnum.ARTICLE_REWARD;

/**
 * @author Xuxinyuan
 */
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, Article> implements ArticleService {
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private TagDao tagDao;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleTagDao articleTagDao;
    @Autowired
    private FollowDao followDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private SearchStrategyContext searchStrategyContext;
    @Autowired
    private HttpSession session;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private InfoService blogInfoService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private RewardPointService rewardPointService;
    @Autowired
    private ProjectServiceImpl projectService;
    @Autowired
    private UploadStrategyContext uploadStrategyContext;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public PageResult<ArchiveDTO> listArchives(ConditionVO conditionVO) {
        if (Objects.isNull(conditionVO.getCategoryId())) {
            conditionVO.setCategoryId(2);
        }
        Page<Article> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<Article>().eq(Article::getIsDelete, FALSE)
                .eq(Objects.nonNull(conditionVO.getUserInfoId()), Article::getUserId, conditionVO.getUserInfoId())
                .eq(Objects.nonNull(conditionVO.getIsReview()), Article::getIsReview, conditionVO.getIsReview())
                .eq(Objects.nonNull(conditionVO.getStatus()), Article::getStatus, conditionVO.getStatus())
                .eq(Article::getCategoryId, conditionVO.getCategoryId());
        if (articleDao.selectCount(lambdaQueryWrapper) == 0) {
            return new PageResult<>();
        }
        // 获取分页数据
        Page<Article> articlePage = articleDao.selectPage(page, lambdaQueryWrapper.select(Article::getId,
                Article::getUserId, Article::getArticleTitle, Article::getArticleContent, Article::getCreateTime)
                .orderByDesc(Article::getCreateTime));
        List<ArchiveDTO> archiveDTOList = BeanCopyUtils.copyList(articlePage.getRecords(), ArchiveDTO.class);
        // 查询评论量
        List<Integer> articleIdList = archiveDTOList.stream().map(ArchiveDTO::getId).collect(Collectors.toList());
        Map<Integer, Integer> commentCountMap = commentDao.listCommentCountByTopicIds(articleIdList, ARTICLE.getType())
                .stream()
                .collect(Collectors.toMap(CommentCountDTO::getId, CommentCountDTO::getCommentCount));
        // 查询点赞量
        Map<String, Object> likeCountMap = redisService.hGetAll(RedisPrefixConst.ARTICLE_LIKE_COUNT);
        // 查询是否点赞
        String articleLikeKey = RedisPrefixConst.ARTICLE_USER_LIKE + UserUtils.getLoginUser().getUserInfoId();
        archiveDTOList.forEach(item -> {
            item.setLikeCount((Integer) likeCountMap.get(item.getId().toString()));
            item.setCommentsCount(commentCountMap.get(item.getId()));
            item.setIsLike(redisService.sIsMember(articleLikeKey, item.getId()));
            item.setArticleContent(StringEscapeUtils.unescapeHtml4(item.getArticleContent()));
        });
        return new PageResult<>(archiveDTOList, (int) articlePage.getTotal());
    }

    @Override
    public PageResult<ArticleBackDTO> listArticleBacks(ConditionVO condition) {
        // 查询文章总量
        Integer count = articleDao.countArticleBacks(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台文章
        List<ArticleBackDTO> articleBackDTOList = articleDao.listArticleBacks(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        // 查询文章点赞量和浏览量
        Map<Object, Double> viewsCountMap = redisService.zAllScore(RedisPrefixConst.ARTICLE_VIEWS_COUNT);
        Map<String, Object> likeCountMap = redisService.hGetAll(RedisPrefixConst.ARTICLE_LIKE_COUNT);
        // 封装点赞量和浏览量
        articleBackDTOList.forEach(item -> {
            Double viewsCount = viewsCountMap.get(item.getId());
            if (Objects.nonNull(viewsCount)) {
                item.setViewsCount(viewsCount.intValue());
            }
            item.setLikeCount((Integer) likeCountMap.get(item.getId().toString()));
        });
        return new PageResult<>(articleBackDTOList, count);
    }

    @Override
    public List<Integer> listArticles() {
        List<Integer> list = new ArrayList<>();
        ConditionVO condition = new ConditionVO();
        condition.setCategoryId(2);
        condition.setUserInfoId(UserUtils.getLoginUser().getUserInfoId());
        condition.setIsDelete(0);
        condition.setStatus(2);
        list.add(articleDao.countArticleBacks(condition));
        condition.setStatus(1);
        list.add(articleDao.countArticleBacks(condition));
        condition.setIsReview(1);
        list.add(articleDao.countArticleBacks(condition));
        condition.setIsReview(2);
        list.add(articleDao.countArticleBacks(condition));
        condition.setIsReview(3);
        list.add(articleDao.countArticleBacks(condition));
        return list;
    }

    @Override
    public ArticlePreviewListDTO listArticlesByCondition(ConditionVO condition) {
        // 查询文章
        List<ArticlePreviewDTO> articlePreviewDTOList = articleDao.listArticlesByCondition(PageUtils.getLimitCurrent(),
                PageUtils.getSize(), condition);
        // 搜索条件对应名(标签或分类名)
        String name = null;
        if (Objects.nonNull(condition.getTagId())) {
            name = tagService.getOne(new LambdaQueryWrapper<Tag>()
                    .select(Tag::getTagName).eq(Tag::getId, condition.getTagId())).getTagName();
        }
        return ArticlePreviewListDTO.builder().articlePreviewDTOList(articlePreviewDTOList).name(name).build();
    }

    @Override
    public ArticleDTO getArticleById(Integer articleId) {
        // 查询推荐文章
        CompletableFuture<List<ArticleRecommendDTO>> recommendArticleList = CompletableFuture.supplyAsync(() ->
                articleDao.listRecommendArticles(articleId));

        // 查询最新文章
        CompletableFuture<List<ArticleRecommendDTO>> newestArticleList = CompletableFuture.supplyAsync(() -> {
            List<Article> articleList = articleDao.selectList(new LambdaQueryWrapper<Article>()
                    .select(Article::getId, Article::getArticleTitle, Article::getArticleCover, Article::getCreateTime)
                    .eq(Article::getIsDelete, FALSE)
                    .eq(Article::getStatus, PUBLIC.getStatus()).orderByDesc(Article::getId).last("limit 5"));
            return BeanCopyUtils.copyList(articleList, ArticleRecommendDTO.class);
        });
        // 查询id对应文章
        ArticleDTO article = articleDao.getArticleById(articleId);
        if (Objects.isNull(article)) {
            throw new BizException("文章不存在");
        }
        article.setNickname(userInfoDao.selectById(article.getUserId()).getNickname());
        article.setAvatar(userInfoDao.selectById(article.getUserId()).getAvatar());
        article.setArticleContent(HTMLUtils.filterWithHTML(StringEscapeUtils.unescapeHtml4(article.getArticleContent())));
        // 更新文章浏览量
        updateArticleViewsCount(articleId);
        // 查询上一篇下一篇文章
        Article lastArticle = articleDao.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover).eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus()).eq(Article::getIsReview, true)
                .lt(Article::getId, articleId)
                .orderByDesc(Article::getId).last("limit 1"));
        Article nextArticle = articleDao.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover).eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus()).eq(Article::getIsReview, true)
                .gt(Article::getId, articleId).orderByAsc(Article::getId)
                .last("limit 1"));
        article.setLastArticle(BeanCopyUtils.copyObject(lastArticle, ArticlePaginationDTO.class));
        article.setNextArticle(BeanCopyUtils.copyObject(nextArticle, ArticlePaginationDTO.class));
        // 封装点赞量、浏览量、评论量
        Double score = redisService.zScore(RedisPrefixConst.ARTICLE_VIEWS_COUNT, articleId);
        if (Objects.nonNull(score)) {
            article.setViewsCount(score.intValue());
        }
        article.setLikeCount((Integer) redisService.hGet(RedisPrefixConst.ARTICLE_LIKE_COUNT, articleId.toString()));
        article.setCommentsCount(Math.toIntExact(commentDao.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getType, 1).eq(Comment::getTopicId, articleId).isNull(Comment::getParentId))));
        // 判断是否点赞
        String articleLikeKey = RedisPrefixConst.ARTICLE_USER_LIKE + UserUtils.getLoginUser().getUserInfoId();
        article.setIsLike(redisService.sIsMember(articleLikeKey, articleId));
        // 封装文章信息
        try {
            article.setRecommendArticleList(recommendArticleList.get());
            article.setNewestArticleList(newestArticleList.get());
        } catch (Exception e) {
            log.error(StrUtil.format("堆栈信息:{}", ExceptionUtil.stacktraceToString(e)));
        }
        return article;
    }


    @Override
    public void saveArticleLike(Integer articleId) {
        // 判断是否点赞
        String articleLikeKey = RedisPrefixConst.ARTICLE_USER_LIKE + UserUtils.getLoginUser().getUserInfoId();
        if (redisService.sIsMember(articleLikeKey, articleId)) {
            // 点过赞则删除文章id
            redisService.sRemove(articleLikeKey, articleId);
            // 文章点赞量-1
            redisService.hDecr(RedisPrefixConst.ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        } else {
            // 未点赞则增加文章id
            redisService.sAdd(articleLikeKey, articleId);
            // 文章点赞量+1
            redisService.hIncr(RedisPrefixConst.ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
            Message message = new Message();
            message.setFromId(1);
            message.setToId(articleDao.selectById(articleId).getUserId());
            message.setFromUserId(UserUtils.getLoginUser().getUserInfoId());
            message.setCommentedId(articleId);
            message.setCommentedContent(articleDao.selectById(articleId).getArticleTitle());
            message.setMessageContent("文章");
            message.setConversationCode("我收到的点赞");
            messageService.saveOrUpdate(message);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateArticle(ArticleVO articleVO) {
        // 查询博客配置信息
        CompletableFuture<WebsiteConfigVO> webConfig = CompletableFuture.supplyAsync(() -> blogInfoService.getWebsiteConfig());
        if (Objects.nonNull(articleVO.getId()) && Objects.nonNull(articleVO.getIsDelete()) && articleVO.getIsDelete() == 1) {
            this.saveOrUpdate(BeanCopyUtils.copyObject(articleVO, Article.class));
            return;
        }
        //过滤文章
        articleVO.setArticleTitle(HTMLUtils.filter(articleVO.getArticleTitle()));
        articleVO.setArticleContent(StringEscapeUtils.escapeHtml4(articleVO.getArticleContent()));
        // 保存文章分类
        Category category = saveArticleCategory(articleVO);
        // 保存或修改文章
        Article article = BeanCopyUtils.copyObject(articleVO, Article.class);
        if (Objects.nonNull(category)) {
            article.setCategoryId(category.getId());
        }
        // 设定默认文章封面
        if (StrUtil.isBlank(article.getArticleCover()) && Objects.isNull(article.getId())){
            try {
                article.setArticleCover(webConfig.get().getArticleCover());
            } catch (Exception e) {
                throw new BizException("设定默认文章封面失败");
            }
        }
        if (Objects.isNull(articleVO.getId())) {
            RewardPoint reward = RewardPoint.builder()
                    .points(ARTICLE_REWARD.getPoint())
                    .event("上传文章")
                    .userId(UserUtils.getLoginUser().getUserInfoId())
                    .totalPoints(projectService.getTotalPoints() + ARTICLE_REWARD.getPoint()).build();
            rewardPointService.save(reward);
        }
        article.setUserId(UserUtils.getLoginUser().getUserInfoId());
        article.setIsReview(1);
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setContent("您收到了一条新的文章，请前往后台管理页面审核");
        emailDTO.setEmail("zhang.junge@innolcon.com");
        emailDTO.setSubject("审核提醒");
        rabbitTemplate.convertAndSend(MQPrefixConst.EMAIL_EXCHANGE, "*",
                new org.springframework.amqp.core.Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
        this.saveOrUpdate(article);
        // 保存文章标签
        saveArticleTag(articleVO, article.getId());
    }

    /**
     * 保存文章分类
     *
     * @param articleVO 文章信息
     * @return {@link Category} 文章分类
     */
    private Category saveArticleCategory(ArticleVO articleVO) {
        // 判断分类是否存在
        Category category = categoryDao.selectOne(new LambdaQueryWrapper<Category>().eq(Category::getCategoryName, articleVO.getCategoryName()));
        if (Objects.isNull(category) && !articleVO.getStatus().equals(DRAFT.getStatus())) {
            category = Category.builder().categoryName(articleVO.getCategoryName()).build();
            categoryDao.insert(category);
        }
        return category;
    }

    @Override
    public void updateArticleTop(ArticleTopVO articleTopVO) {
        // 修改文章审核状态
        Article article = Article.builder().id(articleTopVO.getId()).isReview(articleTopVO.getIsReview()).build();
        articleDao.updateById(article);
    }

    @Override
    public void updateArticleDelete(DeleteVO deleteVO) {
        // 修改文章逻辑删除状态
        List<Article> articleList = deleteVO.getIdList().stream().map(id -> Article.builder()
                        .id(id)
                        .isTop(FALSE)
                        .isDelete(deleteVO.getIsDelete())
                        .build())
                .collect(Collectors.toList());
        this.updateBatchById(articleList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteArticles(List<Integer> articleIdList) {
        // 删除文章标签关联
        articleTagDao.delete(new LambdaQueryWrapper<ArticleTag>().in(ArticleTag::getArticleId, articleIdList));
        // 删除文章
        articleDao.deleteBatchIds(articleIdList);
    }

    @Override
    public List<String> exportArticles(List<Integer> articleIdList) {
        // 查询文章信息
        List<Article> articleList = articleDao.selectList(new LambdaQueryWrapper<Article>()
                .select(Article::getArticleTitle, Article::getArticleContent)
                .in(Article::getId, articleIdList));
        // 写入文件并上传
        List<String> urlList = new ArrayList<>();
        for (Article article : articleList) {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(article.getArticleContent().getBytes())) {
                String url = uploadStrategyContext.executeUploadStrategy(article.getArticleTitle() +
                        FileExtEnum.MD.getExtName(), inputStream, FilePathEnum.MD.getPath());
                urlList.add(url);
            } catch (Exception e) {
                log.error(StrUtil.format("导入文章失败,堆栈:{}", ExceptionUtil.stacktraceToString(e)));
                throw new BizException("导出文章失败");
            }
        }
        return urlList;
    }

    @Override
    public List<ArticleSearchDTO> listArticlesBySearch(ConditionVO condition) {
        return searchStrategyContext.executeSearchStrategy(condition.getKeywords());
    }

    @Override
    public ArticleVO getArticleBackById(Integer articleId) {
        // 查询文章信息
        Article article = articleDao.selectById(articleId);
        // 查询文章分类
        Category category = categoryDao.selectById(article.getCategoryId());
        String categoryName = null;
        if (Objects.nonNull(category)) {
            categoryName = category.getCategoryName();
        }
        // 查询文章标签
        List<String> tagNameList = tagDao.listTagNameByArticleId(articleId);
        // 封装数据
        article.setArticleContent(StringEscapeUtils.unescapeHtml4(article.getArticleContent()));
        ArticleVO articleVO = BeanCopyUtils.copyObject(article, ArticleVO.class);
        articleVO.setCategoryName(categoryName);
        articleVO.setTagNameList(tagNameList);
        return articleVO;
    }


    /**
     * 更新文章浏览量
     *
     * @param articleId 文章id
     */
    public void updateArticleViewsCount(Integer articleId) {
        // 判断是否第一次访问，增加浏览量
        Set<Integer> articleSet = CommonUtils.castSet(Optional.ofNullable(session.getAttribute(CommonConst.ARTICLE_SET)).orElseGet(HashSet::new), Integer.class);
        if (!articleSet.contains(articleId)) {
            articleSet.add(articleId);
            session.setAttribute(CommonConst.ARTICLE_SET, articleSet);
            // 浏览量+1
            redisService.zIncr(RedisPrefixConst.ARTICLE_VIEWS_COUNT, articleId, 1D);
        }
    }

    /**
     * 保存文章标签
     *
     * @param articleVO 文章信息
     */
    private void saveArticleTag(ArticleVO articleVO, Integer articleId) {
        // 编辑文章则删除文章所有标签
        if (Objects.nonNull(articleVO.getId())) {
            articleTagDao.delete(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, articleVO.getId()));
        }
        // 添加文章标签
        List<String> tagNameList = articleVO.getTagNameList();
        if (CollectionUtils.isNotEmpty(tagNameList)) {
            // 查询已存在的标签
            List<Tag> existTagList = tagService.list(new LambdaQueryWrapper<Tag>().in(Tag::getTagName, tagNameList));
            List<String> existTagNameList = existTagList.stream().map(Tag::getTagName).collect(Collectors.toList());
            List<Integer> existTagIdList = existTagList.stream().map(Tag::getId).collect(Collectors.toList());
            // 对比新增不存在的标签
            tagNameList.removeAll(existTagNameList);
            if (CollectionUtils.isNotEmpty(tagNameList)) {
                List<Tag> tagList = tagNameList.stream().map(item -> Tag.builder().tagName(item).build()).collect(Collectors.toList());
                tagService.saveBatch(tagList);
                List<Integer> tagIdList = tagList.stream().map(Tag::getId).collect(Collectors.toList());
                existTagIdList.addAll(tagIdList);
            }
            // 提取标签id绑定文章
            List<ArticleTag> articleTagList = existTagIdList.stream().map(item -> ArticleTag.builder()
                            .articleId(articleId)
                            .tagId(item)
                            .build())
                    .collect(Collectors.toList());
            articleTagService.saveBatch(articleTagList);
        }
    }

    @Override
    public List<ArticleRankDTO> articlesRank(){
        Map<Object, Double> articleMap = redisService.zReverseRangeWithScore(RedisPrefixConst.ARTICLE_VIEWS_COUNT, 0, 4);
        if (CollectionUtils.isNotEmpty(articleMap)) {
            // 查询文章排行
            return listArticleRank(articleMap);
        }
        return null;
    }

    /**
     * 查询文章排行
     *
     * @param articleMap 文章信息
     * @return {@link List<ArticleRankDTO>} 文章排行
     */
    private List<ArticleRankDTO> listArticleRank(Map<Object, Double> articleMap) {
        // 提取文章id
        List<Integer> articleIdList = new ArrayList<>(articleMap.size());
        articleMap.forEach((key, value) -> articleIdList.add((Integer) key));
        // 查询文章信息
        return articleDao.selectList(new LambdaQueryWrapper<Article>()
                        .select(Article::getId, Article::getArticleTitle, Article::getArticleContent)
                        .in(Article::getId, articleIdList))
                .stream().map(article -> ArticleRankDTO.builder()
                        .id(article.getId())
                        .articleContent(StringEscapeUtils.unescapeHtml4(article.getArticleContent()))
                        .articleTitle(article.getArticleTitle())
                        .viewsCount(articleMap.get(article.getId()).intValue())
                        .build())
                .sorted(Comparator.comparingInt(ArticleRankDTO::getViewsCount).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<ArchiveDTO> articlesFollow(Integer size, Integer current){
        List<ArchiveDTO> articleList = new ArrayList<>();
        List<Article> articles = articleDao.selectList(new LambdaQueryWrapper<Article>().eq(Article::getIsDelete, 0)
                        .eq(Article::getStatus, 1).orderByDesc(Article::getCreateTime).eq(Article::getIsReview, 3));
        List<Follow> followList = followDao.selectList(new LambdaQueryWrapper<Follow>()
                .eq(Follow::getUserInfoId, UserUtils.getLoginUser().getUserInfoId()));
        List<Integer> followIdList = new ArrayList<>();
        for (Follow follow : followList) {
            followIdList.add(follow.getFollowId());
        }
        for (Article article : articles) {
            if (followIdList.contains(article.getUserId())) {
                articleList.add(BeanCopyUtils.copyObject(article, ArchiveDTO.class));
            }
        }
        if (articleList.size() == 0) {
            return new PageResult<>();
        }
        int fromIndex = (current - 1) * size;
        int toIndex = articleList.size() - fromIndex > size ? fromIndex + size : articleList.size();
        List<ArchiveDTO> archiveDTOS = articleList.subList(fromIndex, toIndex);
        return new PageResult<>(archiveDTOS, articleList.size());
    }

    @Override
    public Set<ArchiveDTO> articlesRecommend(){
        List<ArchiveDTO> articles = BeanCopyUtils.copyList(articleDao.selectList(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, FALSE).eq(Article::getStatus, 1).eq(Article::getIsReview, true)), ArchiveDTO.class);
        Random random = new Random(UserUtils.getLoginUser().getUserInfoId() + System.currentTimeMillis());
        Set<ArchiveDTO> archiveDTOS = new HashSet<>();
        if (articles.size() < 5) {
            archiveDTOS.addAll(articles);
            return archiveDTOS;
        }
        for (int i = 0; i < 5; ) {
            archiveDTOS.add(articles.get(random.nextInt(articles.size())));
            i = archiveDTOS.size();
        }
        return archiveDTOS;
    }

    @Override
    public ComposeHomeDTO composeHome() {
        ComposeHomeDTO composeHomeDTO = new ComposeHomeDTO();
        composeHomeDTO.setArticleCount(Math.toIntExact(articleDao.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, CommonConst.FALSE)
                .eq(Article::getUserId, UserUtils.getLoginUser().getUserInfoId()))));
        composeHomeDTO.setArticlesVisibleCount((Math.toIntExact(articleDao.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, CommonConst.FALSE).eq(Article::getIsReview, true).eq(Article::getStatus, 1)
                .eq(Article::getUserId, UserUtils.getLoginUser().getUserInfoId())))));
        composeHomeDTO.setFollowers(Math.toIntExact(followDao.selectCount(new LambdaQueryWrapper<Follow>()
                .eq(Follow::getFollowId, UserUtils.getLoginUser().getUserInfoId()))));
        int viewsCount = 0, commentCount = 0, likesCount = 0;
        List<ArchiveDTO> archiveDTOList = BeanCopyUtils.copyList(articleDao.selectList(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getCreateTime).orderByDesc(Article::getCreateTime)
                .eq(Article::getIsDelete, CommonConst.FALSE)
                .eq(Article::getUserId, UserUtils.getLoginUser().getUserInfoId())), ArchiveDTO.class);
        // 查询评论量
        List<Integer> articleIdList = archiveDTOList.stream().map(ArchiveDTO::getId).collect(Collectors.toList());
        Map<Integer, Integer> commentCountMap = commentDao.listCommentCountByTopicIds(articleIdList, ARTICLE.getType())
                .stream().collect(Collectors.toMap(CommentCountDTO::getId, CommentCountDTO::getCommentCount));
        // 查询点赞量
        Map<String, Object> likeCountMap = redisService.hGetAll(RedisPrefixConst.ARTICLE_LIKE_COUNT);
        // 查询浏览量
        Map<Object, Double> viewsCountMap = redisService.zAllScore(RedisPrefixConst.ARTICLE_VIEWS_COUNT);
        for (Integer integer : articleIdList) {
            if (Objects.nonNull(likeCountMap.get(integer.toString()))) {
                likesCount += (Integer) likeCountMap.get(integer.toString());
            }
            if (Objects.nonNull(commentCountMap.get(integer))) {
                commentCount += commentCountMap.get(integer);
            }
            if (Objects.nonNull(viewsCountMap.get(integer))) {
                viewsCount += viewsCountMap.get(integer).intValue();
            }
        }
        composeHomeDTO.setLikesCount(likesCount);
        composeHomeDTO.setCommentsCount(commentCount);
        composeHomeDTO.setViewsCount(viewsCount);
        return composeHomeDTO;
    }
}
