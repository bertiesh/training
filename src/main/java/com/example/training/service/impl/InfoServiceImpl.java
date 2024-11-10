package com.example.training.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.training.constant.CommonConst;
import com.example.training.constant.RedisPrefixConst;
import com.example.training.dao.*;
import com.example.training.dto.*;
import com.example.training.entity.*;
import com.example.training.service.InfoService;
import com.example.training.service.RedisService;
import com.example.training.service.UniqueViewService;
import com.example.training.util.BeanCopyUtils;
import com.example.training.util.IpUtils;
import com.example.training.vo.InfoVO;
import com.example.training.vo.WebsiteConfigVO;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Xuxinyuan
 */
@Service
public class InfoServiceImpl implements InfoService {
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private TagDao tagDao;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private FollowDao followDao;
    @Autowired
    private TalkDao talkDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserAuthDao userAuthDao;
    @Autowired
    private UniqueViewService uniqueViewService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private WebsiteConfigDao websiteConfigDao;
    @Resource
    private HttpServletRequest request;

    @Override
    public HomeInfoDTO getBlogHomeInfo(Integer userId) {
        //查询用户信息
        UserInfo userInfo = userInfoDao.selectById(userId);
        UserInfoDTO userInfoDTO = UserInfoDTO.builder().userInfoId(userId).phone(userInfo.getPhone())
                .email(userInfo.getEmail()).roleList(roleDao.listRolesByUserInfoId(userInfo.getId()))
                .nickname(userInfo.getNickname()).avatar(userInfo.getAvatar()).intro(userInfo.getIntro())
                .hospital(userInfo.getHospital()).qualifications(userInfo.getQualifications()).roleList(null)
                .articleLikeSet(redisService.sMembers(RedisPrefixConst.ARTICLE_USER_LIKE + userInfo.getId()))
                .commentLikeSet(redisService.sMembers(RedisPrefixConst.COMMENT_USER_LIKE + userInfo.getId()))
                .talkLikeSet(redisService.sMembers(RedisPrefixConst.TALK_USER_LIKE + userInfo.getId()))
                .usersFollowed(followDao.selectList(new LambdaQueryWrapper<Follow>().eq(Follow::getUserInfoId, userInfo.getId())))
                .followers(followDao.selectList(new LambdaQueryWrapper<Follow>().eq(Follow::getFollowId, userInfo.getId())))
                .ipSource(userAuthDao.selectOne(new LambdaQueryWrapper<UserAuth>().eq(UserAuth::getUserInfoId, userInfo.getId())).getIpSource())
                .build();
        // 查询文章数量
        Integer articleCount = Math.toIntExact(articleDao.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, CommonConst.FALSE).eq(Article::getUserId, userInfo.getId())));

        //查询自己最近文章标题合集
        List<ArchiveDTO> archiveDTOList = BeanCopyUtils.copyList(articleDao.selectList(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getCreateTime).orderByDesc(Article::getCreateTime)
                .eq(Article::getIsDelete, CommonConst.FALSE).eq(Article::getUserId, userInfo.getId())), ArchiveDTO.class);

        //查询自己最近讨论id合集
        List<Integer> myTalkIdList = new ArrayList<>();
        List<Talk> talkList = talkDao.selectList(new LambdaQueryWrapper<Talk>().orderByDesc(Talk::getCreateTime)
                .eq(Talk::getUserId, userInfo.getId()));
        for (Talk talk : talkList) {
            myTalkIdList.add(talk.getId());
        }

        // 封装数据
        return HomeInfoDTO.builder().articleCount(articleCount).myArchiveDTOList(archiveDTOList)
                .myTalkIdList(myTalkIdList).userInfoDTO(userInfoDTO).build();
    }

    @Override
    public BackInfoDTO getBlogBackInfo(String start, String end) {
        // 查询访问量
        Object count = redisService.get(RedisPrefixConst.BLOG_VIEWS_COUNT);
        Integer viewsCount = Integer.parseInt(Optional.ofNullable(count).orElse(0).toString());
        // 查询留言量
        Integer messageCount = Math.toIntExact(messageDao.selectCount(new LambdaQueryWrapper<Message>()
                .eq(Message::getToId, 1).ne(Message::getFromId, 1)));
        // 查询用户量
        Integer userCount = Math.toIntExact(userInfoDao.selectCount(null));
        // 查询文章量
        Integer articleCount = Math.toIntExact(articleDao.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, CommonConst.FALSE)));
        // 查询一周用户量
        List<UniqueViewDTO> uniqueViewList = uniqueViewService.listUniqueViews();
        // 查询文章统计
        List<ArticleStatisticsDTO> articleStatisticsList = articleDao.listArticleStatistics();
        // 查询分类数据
        List<CategoryDTO> categoryDTOList = categoryDao.listCategoryDTO();
        // 查询标签数据
        List<TagDTO> tagDTOList = BeanCopyUtils.copyList(tagDao.selectList(null), TagDTO.class);
        // 查询redis访问量前五的文章
        Map<Object, Double> articleMap = redisService.zReverseRangeWithScore(RedisPrefixConst.ARTICLE_VIEWS_COUNT, 0, 4);
        BackInfoDTO blogBackInfoDTO = BackInfoDTO.builder()
                .articleStatisticsList(articleStatisticsList)
                .tagDTOList(tagDTOList)
                .viewsCount(viewsCount)
                .messageCount(messageCount)
                .userCount(userCount)
                .articleCount(articleCount)
                .categoryDTOList(categoryDTOList)
                .uniqueViewDTOList(uniqueViewList)
                .build();
        if (CollectionUtils.isNotEmpty(articleMap)) {
            // 查询文章排行
            List<ArticleRankDTO> articleRankDTOList = listArticleRank(articleMap);
            blogBackInfoDTO.setArticleRankDTOList(articleRankDTOList);
        }
        return blogBackInfoDTO;
    }

    @Override
    public void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO) {
        // 修改网站配置
        WebsiteConfig websiteConfig = WebsiteConfig.builder()
                .id(1)
                .config(JSON.toJSONString(websiteConfigVO))
                .build();
        websiteConfigDao.updateById(websiteConfig);
        // 删除缓存
        redisService.del(RedisPrefixConst.WEBSITE_CONFIG);
    }

    @Override
    public WebsiteConfigVO getWebsiteConfig() {
        WebsiteConfigVO websiteConfigVO;
        // 获取缓存数据
        Object websiteConfig = redisService.get(RedisPrefixConst.WEBSITE_CONFIG);
        if (Objects.nonNull(websiteConfig)) {
            websiteConfigVO = JSON.parseObject(websiteConfig.toString(), WebsiteConfigVO.class);
        } else {
            // 从数据库中加载
            String config = websiteConfigDao.selectById(CommonConst.DEFAULT_CONFIG_ID).getConfig();
            websiteConfigVO = JSON.parseObject(config, WebsiteConfigVO.class);
            redisService.set(RedisPrefixConst.WEBSITE_CONFIG, config);
        }
        return websiteConfigVO;
    }

    @Override
    public String getAbout() {
        Object value = redisService.get(RedisPrefixConst.ABOUT);
        return Objects.nonNull(value) ? value.toString() : "";
    }

    @Override
    public void updateAbout(InfoVO blogInfoVO) {
        redisService.set(RedisPrefixConst.ABOUT, blogInfoVO.getAboutContent());
    }

    @Override
    public WebsiteDTO report() {
        // 获取ip
        String ipAddress = IpUtils.getIpAddress(request);
        // 获取访问设备
        UserAgent userAgent = IpUtils.getUserAgent(request);
        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        // 生成唯一用户标识
        String uuid = ipAddress + browser.getName() + operatingSystem.getName();
        String md5 = DigestUtils.md5DigestAsHex(uuid.getBytes());
        // 判断是否访问
        if (!redisService.sIsMember(RedisPrefixConst.UNIQUE_VISITOR, md5)) {
            // 统计游客地域分布
            String ipSource = IpUtils.getIpSource(ipAddress);
            if (StringUtils.isNotBlank(ipSource)) {
                ipSource = ipSource.substring(0, 2)
                        .replaceAll(CommonConst.PROVINCE, "")
                        .replaceAll(CommonConst.CITY, "");
                redisService.hIncr(RedisPrefixConst.VISITOR_AREA, ipSource, 1L);
            } else {
                redisService.hIncr(RedisPrefixConst.VISITOR_AREA, CommonConst.UNKNOWN, 1L);
            }
            // 访问量+1
            redisService.incr(RedisPrefixConst.BLOG_VIEWS_COUNT, 1);
            // 保存唯一标识
            redisService.sAdd(RedisPrefixConst.UNIQUE_VISITOR, md5);
        }
        WebsiteDTO websiteDTO;
        // 获取缓存数据
        Object websiteConfig = redisService.get(RedisPrefixConst.WEBSITE_CONFIG);
        if (Objects.nonNull(websiteConfig)) {
            websiteDTO = JSON.parseObject(websiteConfig.toString(), WebsiteDTO.class);
        } else {
            // 从数据库中加载
            String config = websiteConfigDao.selectById(CommonConst.DEFAULT_CONFIG_ID).getConfig();
            websiteDTO = JSON.parseObject(config, WebsiteDTO.class);
            redisService.set(RedisPrefixConst.WEBSITE_CONFIG, config);
        }
        return websiteDTO;
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
                        .select(Article::getId, Article::getArticleTitle)
                        .in(Article::getId, articleIdList))
                .stream().map(article -> ArticleRankDTO.builder()
                        .articleTitle(article.getArticleTitle())
                        .viewsCount(articleMap.get(article.getId()).intValue())
                        .build())
                .sorted(Comparator.comparingInt(ArticleRankDTO::getViewsCount).reversed())
                .collect(Collectors.toList());
    }
}
