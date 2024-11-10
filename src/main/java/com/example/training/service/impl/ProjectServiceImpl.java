package com.example.training.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.constant.CommonConst;
import com.example.training.dao.*;
import com.example.training.dto.*;
import com.example.training.entity.*;
import com.example.training.exception.BizException;
import com.example.training.service.PostService;
import com.example.training.service.ProjectService;
import com.example.training.service.RewardPointService;
import com.example.training.util.*;
import com.example.training.vo.ConditionVO;
import com.example.training.vo.PageResult;
import com.example.training.vo.PostVO;
import com.example.training.vo.ProjectVO;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.training.enums.RewardPointsEnum.SURVEY_REWARD;

/**
 * @author Xuxinyuan
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectDao, Project> implements ProjectService {
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private AnswerDao answerDao;
    @Autowired
    private QuestionTagDao questionTagDao;
    @Autowired
    private TagDao tagDao;
    @Autowired
    private PostDao postDao;
    @Autowired
    private RewardPointDao rewardPointDao;
    @Autowired
    private RewardPointService rewardPointService;
    @Autowired
    private PostService postService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateProject(ProjectVO projectVO) {
        // 查询项目名是否存在
        Project project = projectDao.selectOne(new LambdaQueryWrapper<Project>()
                .select(Project::getId).eq(Project::getName, projectVO.getName()));
        if (Objects.nonNull(project) && Objects.isNull(projectVO.getId())) {
            throw new BizException("项目名已存在");
        }
        project = BeanCopyUtils.copyObject(projectVO, Project.class);
        if (Objects.nonNull(projectVO.getPassword())) {
            project.setPassword(BCrypt.hashpw(project.getPassword(), BCrypt.gensalt()));
        }
        // 随机问题处理
        if (project.getIsRandom()) {
            List<Integer> idList = new ArrayList<>();
            // 根据标签随机
            if (!Objects.equals(project.getTagIds(), "")){
                    List<QuestionTag> questionTags = questionTagDao.selectList(
                            new LambdaQueryWrapper<QuestionTag>().in(QuestionTag::getTagId,
                            CommonUtils.castList(JSON.parseObject(project.getTagIds(), List.class), Integer.class)));
                    questionTags.forEach(questionTag -> {
                        if (questionDao.selectById(questionTag.getQuestionId()).getIsDelete() == 0) {
                            idList.add(questionTag.getQuestionId());
                        }
                    });
            } else {
                questionDao.selectList(new LambdaQueryWrapper<Question>().eq(Question::getIsDelete, false)).forEach(
                        question -> idList.add(question.getId()));
            }
            List<Integer> newList = idList.stream().distinct().collect(Collectors.toList());
            List<Integer> ids = new ArrayList<>();
            // 根据题型随机
            if (!Objects.equals(project.getTypes(), "")) {
                String types = project.getTypes();
                newList.forEach(integer -> {
                    if (CommonUtils.castList(JSON.parseObject(types, List.class), Integer.class)
                            .contains(questionDao.selectById(integer).getType())) {
                        ids.add(integer);
                    }
                });
                newList = ids;
            }
            // 控制随机数量
            if (project.getQuestionNum() > 0 && newList.size() > project.getQuestionNum()) {
                while (newList.size() > project.getQuestionNum()) {
                    Random random = new Random();
                    newList.remove(random.nextInt(newList.size()));
                }
            }
            project.setQuestions(JSON.toJSONString(newList));
        }
        this.saveOrUpdate(project);
    }

    @Override
    public PageResult<ProjectBackDTO> listProjectBacks(ConditionVO condition) {
        // 查询模板数量
        int count = Math.toIntExact(projectDao.selectCount(new LambdaQueryWrapper<Project>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), Project::getName, condition.getKeywords())
                .eq(Project::getIsDelete, CommonConst.FALSE)
                .eq(Objects.nonNull(condition.getStatus()), Project::getStatus, condition.getStatus())));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询模板信息
        List<ProjectBackDTO> projectBackDTOList = projectDao.listProjectBacks(PageUtils.getLimitCurrent(),
                PageUtils.getSize(), condition);
        projectBackDTOList.forEach(projectBackDTO -> {
            projectBackDTO = getQuestions(projectBackDTO);
        });
        return new PageResult<>(projectBackDTOList, count);
    }

    /**
     * 添加项目中底层的题目、答案、标签嵌套
     * @param projectBackDTO 项目信息
     * @return ProjectBackDTO
     */
    ProjectBackDTO getQuestions(ProjectBackDTO projectBackDTO) {
        if (Objects.nonNull(projectBackDTO.getQuestions())) {
            List<Integer> list = CommonUtils.castList(JSON.parseObject(projectBackDTO.getQuestions(), List.class), Integer.class);
            List<QuestionBackDTO> questionBackDTOList = new ArrayList<>();
            for (Integer integer : list) {
                QuestionBackDTO questionBackDTO = BeanCopyUtils.copyObject(questionDao.selectById(integer), QuestionBackDTO.class);
                AnswerBackDTO answerBackDTO = BeanCopyUtils.copyObject(answerDao.selectById(integer), AnswerBackDTO.class);
                // 转换可能答案格式
                if (Objects.nonNull(answerBackDTO.getPossibleAnswers())) {
                    answerBackDTO.setPossibleAnswers(StringEscapeUtils.unescapeHtml4(answerBackDTO.getPossibleAnswers()));
                    if (answerBackDTO.getPossibleAnswers().startsWith("[")) {
                        answerBackDTO.setPossibleAnswerList(CommonUtils.castList(JSON.parseObject(answerBackDTO.getPossibleAnswers(),
                                List.class), String.class));
                    }
                }
                // 转换正确答案格式
                if (Objects.nonNull(answerBackDTO.getCorrectAnswers())) {
                    answerBackDTO.setCorrectAnswers(StringEscapeUtils.unescapeHtml4(answerBackDTO.getCorrectAnswers()));
                    answerBackDTO.setCorrectAnswerList(CommonUtils.castList(JSON.parseObject(answerBackDTO.getCorrectAnswers(),
                            List.class), String.class));
                }
                questionBackDTO.setAnswerBackDTO(answerBackDTO);
                List<Integer> idList = new ArrayList<>();
                questionTagDao.selectList(new LambdaQueryWrapper<QuestionTag>().eq(QuestionTag::getQuestionId, integer))
                        .forEach(questionTag -> idList.add(questionTag.getTagId()));
                List<TagDTO> tagDTOList = new ArrayList<>();
                idList.forEach(id -> tagDTOList.add(BeanCopyUtils.copyObject(tagDao.selectById(id), TagDTO.class)));
                questionBackDTO.setTagDTOList(tagDTOList);
                questionBackDTOList.add(questionBackDTO);
            }
            projectBackDTO.setQuestionBackDTOS(questionBackDTOList);
        }
        if (Objects.nonNull(projectBackDTO.getCascades())) {
            Map<Integer, Map<Integer, Integer>> map = JSON.parseObject(projectBackDTO.getCascades(),
                    new TypeReference<HashMap<Integer,HashMap<Integer, Integer>>>() {});
            projectBackDTO.setCascadesMapList(map);
        }
        return projectBackDTO;
    }

    @Override
    public ProjectBackDTO getProjectBackById(Integer projectId) {
        // 查询模板信息
        Project project = projectDao.selectById(projectId);
        ProjectBackDTO projectBackDTO = BeanCopyUtils.copyObject(project, ProjectBackDTO.class);
        return getQuestions(projectBackDTO);
    }

    @Override
    public ProjectDTO getProjectById(Integer projectId, String password) {
        // 查询模板信息
        // 校验密码
        Project project = projectDao.selectById(projectId);
        if (project.getIsPassword()) {
            if (!BCrypt.checkpw(password, project.getPassword())) {
                throw new BizException("密码错误");
            }
        }
        // 是否已开放
        if (!project.getIsActive() ||
                (project.getEndTime() != null && project.getEndTime().isBefore(LocalDateTime.now())) ||
                (project.getStartTime() != null && project.getStartTime().isAfter(LocalDateTime.now()))) {
            throw new BizException("项目未开放或已经截止");
        }
        ProjectDTO projectDTO = BeanCopyUtils.copyObject(project, ProjectDTO.class);
        if (Objects.nonNull(projectDTO.getQuestions())) {
            List<Integer> list = CommonUtils.castList(JSON.parseObject(projectDTO.getQuestions(), List.class), Integer.class);
            List<QuestionDTO> questionDTOList = new ArrayList<>();
            for (Integer integer : list) {
                QuestionDTO questionDTO = BeanCopyUtils.copyObject(questionDao.selectById(integer), QuestionDTO.class);
                questionDTO.setPossibleAnswers(answerDao.selectById(integer).getPossibleAnswers());
                if (Objects.nonNull(questionDTO.getPossibleAnswers())) {
                    questionDTO.setPossibleAnswers(StringEscapeUtils.unescapeHtml4(questionDTO.getPossibleAnswers()));
                    if (questionDTO.getPossibleAnswers().startsWith("[")) {
                        questionDTO.setPossibleAnswerList(CommonUtils.castList(JSON.parseObject(questionDTO.getPossibleAnswers(),
                                List.class), String.class));
                    }
                }
                questionDTOList.add(questionDTO);
            }
            projectDTO.setQuestionDTOs(questionDTOList);
        }
        if (Objects.nonNull(projectDTO.getCascades())) {
            Map<Integer, Map<Integer, Integer>> map = JSON.parseObject(projectDTO.getCascades(),
                    new TypeReference<HashMap<Integer,HashMap<Integer, Integer>>>() {});
            projectDTO.setCascadesMapList(map);
        }
        return projectDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<QuestionPostDTO> saveOrUpdateProjectPost(PostVO postVO) {
        // 查询项目回答是否存在
        if (!projectDao.selectById(postVO.getProjectId()).getEnableUpdate()){
            if (postDao.selectCount(new LambdaQueryWrapper<Post>().eq(Post::getProjectId, postVO.getProjectId())
                    .eq(Post::getUserId, UserUtils.getLoginUser().getUserInfoId())) > 0) {
                throw new BizException("该项目不允许更新答案");
            }
        }
        Post post = postDao.selectOne(new LambdaQueryWrapper<Post>().eq(Post::getProjectId, postVO.getProjectId())
                .eq(Post::getUserId, UserUtils.getLoginUser().getUserInfoId()).eq(Post::getIsDelete, false));
        if (Objects.nonNull(post)) {
            post.setIsDelete(1);
            postService.saveOrUpdate(post);
        }
        postVO.setAnswer(StringEscapeUtils.escapeHtml4(postVO.getAnswer()));
        post = BeanCopyUtils.copyObject(postVO, Post.class);
        post.setUserId(UserUtils.getLoginUser().getUserInfoId());
        if (postDao.selectCount(new LambdaQueryWrapper<Post>().eq(Post::getProjectId, postVO.getProjectId())
                .eq(Post::getUserId, UserUtils.getLoginUser().getUserInfoId())) == 0) {
            RewardPoint reward = RewardPoint.builder()
                    .points(SURVEY_REWARD.getPoint())
                    .event("完成问卷")
                    .userId(UserUtils.getLoginUser().getUserInfoId())
                    .totalPoints(getTotalPoints() + SURVEY_REWARD.getPoint()).build();
            rewardPointService.save(reward);
        }
        postService.saveOrUpdate(post);
        if (!projectDao.selectById(post.getProjectId()).getAnswerAnalysis()) {
            return null;
        }
        post.setAnswer(StringEscapeUtils.unescapeHtml4(post.getAnswer()));
        return getQuestionPostDTOS(post);
    }

    public int getTotalPoints() {
        int total = 0;
        if (rewardPointDao.selectCount(new LambdaQueryWrapper<RewardPoint>().eq(RewardPoint::getUserId,
                UserUtils.getLoginUser().getUserInfoId())) > 0) {
            total = rewardPointDao.selectList(new LambdaQueryWrapper<RewardPoint>().orderByDesc(RewardPoint::getId)
                            .eq(RewardPoint::getUserId, UserUtils.getLoginUser().getUserInfoId())).get(0)
                    .getTotalPoints();
        }
        return total;
    }

    @Override
    public List<QuestionPostDTO> getProjectHistoryById(Integer projectId) {
        Post post = postDao.selectOne(new LambdaQueryWrapper<Post>().eq(Post::getProjectId, projectId)
                .eq(Post::getUserId, UserUtils.getLoginUser().getUserInfoId()).eq(Post::getIsDelete, false));
        if (Objects.isNull(post)) {
            throw new BizException("未作答");
        }
        if (!projectDao.selectById(projectId).getAnswerAnalysis()) {
            return null;
        }
        post.setAnswer(StringEscapeUtils.unescapeHtml4(post.getAnswer()));
        return getQuestionPostDTOS(post);
    }

    @Override
    public List<QuestionBackAnalysisDTO> getProjectBackAnalysisById(Integer projectId) {
        List<QuestionBackAnalysisDTO> analysisDTOS = new ArrayList<>();
        if (Objects.nonNull(projectDao.selectById(projectId).getQuestions())) {
            for (Integer integer : CommonUtils.castList(JSON.parseObject(projectDao.selectById(projectId).getQuestions(),
                    List.class), Integer.class)) {
                QuestionBackAnalysisDTO questionBackAnalysisDTO = BeanCopyUtils.copyObject(questionDao.selectById(integer),
                        QuestionBackAnalysisDTO.class);
                // 转换图片格式
                if (Objects.nonNull(questionBackAnalysisDTO.getImages())) {
                    questionBackAnalysisDTO.setImgList(CommonUtils.castList(JSON.parseObject(questionBackAnalysisDTO.getImages(),
                            List.class), String.class));
                }
                questionBackAnalysisDTO.setSubmitNums(Math.toIntExact(postDao.selectCount(new LambdaQueryWrapper<Post>()
                        .eq(Post::getIsDelete, 0).eq(Post::getProjectId, projectId)
                        .like(Post::getAnswer, "&quot;" + integer + "&quot;:"))));
                AnalysisDTO analysisDTO = BeanCopyUtils.copyObject(answerDao.selectById(integer), AnalysisDTO.class);
                // 转换正确答案格式
                if (Objects.nonNull(analysisDTO.getCorrectAnswers())) {
                    analysisDTO.setCorrectAnswers(StringEscapeUtils.unescapeHtml4(analysisDTO.getCorrectAnswers()));
                    analysisDTO.setCorrectAnswerList(CommonUtils.castList(JSON.parseObject(analysisDTO.getCorrectAnswers(),
                            List.class), String.class));
                    int correct = 0;
                    for (Post post1 : postDao.selectList(new LambdaQueryWrapper<Post>().eq(Post::getProjectId, projectId)
                            .eq(Post::getIsDelete, false))) {
                        JSONObject jsonObject1 = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(post1.getAnswer()));
                        if (jsonObject1.containsKey(String.valueOf(integer)) &&
                                jsonObject1.getJSONArray(String.valueOf(integer)).equals(analysisDTO.getCorrectAnswerList())) {
                            correct++;
                        }
                    }
                    analysisDTO.setCorrectness(correct/(double)questionBackAnalysisDTO.getSubmitNums());
                    if (questionBackAnalysisDTO.getSubmitNums() == 0) {
                        analysisDTO.setCorrectness(0.0);
                    }
                }
                // 转换可能答案格式
                if (Objects.nonNull(analysisDTO.getPossibleAnswers())) {
                    analysisDTO.setPossibleAnswers(StringEscapeUtils.unescapeHtml4(analysisDTO.getPossibleAnswers()));
                    List<Map<String, Double>> possibleAnswersList = new ArrayList<>();
                    if (analysisDTO.getPossibleAnswers().startsWith("[")) {
                        for (String possibleAnswer : CommonUtils.castList(JSON.parseObject(analysisDTO.getPossibleAnswers()
                                , List.class), String.class)) {
                            Map<String, Double> answerMap = new HashMap<>();
                            int choose = 0;
                            for (Post post1 : postDao.selectList(new LambdaQueryWrapper<Post>().eq(Post::getProjectId, projectId)
                                    .eq(Post::getIsDelete, false))) {
                                JSONObject jsonObject1 = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(post1.getAnswer()));
                                if (jsonObject1.containsKey(String.valueOf(integer)) &&
                                        jsonObject1.getJSONArray(String.valueOf(integer)).contains(possibleAnswer)) {
                                    choose++;
                                }
                            }
                            answerMap.put(possibleAnswer, (choose/(double)questionBackAnalysisDTO.getSubmitNums()));
                            if (questionBackAnalysisDTO.getSubmitNums() == 0) {
                                answerMap.put(possibleAnswer, 0.0);
                            }
                            possibleAnswersList.add(answerMap);
                        }
                    } else {
                        Map<String, Double> answerMap = new HashMap<>();
                        answerMap.put(analysisDTO.getPossibleAnswers(), 0.0);
                        possibleAnswersList.add(answerMap);
                    }
                    analysisDTO.setPossibleAnswerList(possibleAnswersList);
                } else {
                    //  填空提交统计
                    Map<String, Integer> submits = new HashMap<>();
                    for (Post post1 : postDao.selectList(new LambdaQueryWrapper<Post>().eq(Post::getProjectId, projectId)
                            .eq(Post::getIsDelete, false))) {
                        JSONObject jsonObject1 = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(post1.getAnswer()));
                        if (jsonObject1.containsKey(String.valueOf(integer))) {
                            String answer = jsonObject1.getJSONArray(String.valueOf(integer)).getString(0);
                            if (submits.containsKey(answer)) {
                                submits.replace(answer, submits.get(answer) + 1);
                            } else {
                                submits.put(answer, 1);
                            }
                        }
                    }
                    analysisDTO.setSubmits(submits);
                }
                questionBackAnalysisDTO.setAnalysisDTO(analysisDTO);
                analysisDTOS.add(questionBackAnalysisDTO);
            }
        }
        return analysisDTOS;
    }

    public List<QuestionPostDTO> getQuestionPostDTOS(Post post) {
        // 获取每一道题的回答
        List<QuestionPostDTO> questionPostDTOS = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(post.getAnswer());
        for (String s : jsonObject.keySet()) {
            int questionId = Integer.parseInt(s);
            List<String> myAnswers = jsonObject.getJSONArray(s);
            QuestionPostDTO questionPostDTO = BeanCopyUtils.copyObject(questionDao.selectById(questionId), QuestionPostDTO.class);
            // 转换图片格式
            if (Objects.nonNull(questionPostDTO.getImages())) {
                questionPostDTO.setImgList(CommonUtils.castList(JSON.parseObject(questionPostDTO.getImages(), List.class), String.class));
            }
            // 获取答案
            if (Objects.nonNull(answerDao.selectById(questionId))) {
                AnswerDTO answerDTO = BeanCopyUtils.copyObject(answerDao.selectById(questionId), AnswerDTO.class);
                // 转换图片格式
                if (Objects.nonNull(answerDTO.getImages())) {
                    answerDTO.setImgList(CommonUtils.castList(JSON.parseObject(answerDTO.getImages(), List.class), String.class));
                }
                // 转换可能答案格式
                if (Objects.nonNull(answerDTO.getPossibleAnswers())) {
                    answerDTO.setPossibleAnswers(StringEscapeUtils.unescapeHtml4(answerDTO.getPossibleAnswers()));
                    List<Map<String, Double>> possibleAnswersList = new ArrayList<>();
                    if (answerDTO.getPossibleAnswers().startsWith("[")) {
                        for (String possibleAnswer : CommonUtils.castList(JSON.parseObject(answerDTO.getPossibleAnswers(), List.class), String.class)) {
                            Map<String, Double> answerMap = new HashMap<>();
                            long total = postDao.selectCount(new LambdaQueryWrapper<Post>().eq(Post::getProjectId, post.getProjectId())
                                    .eq(Post::getIsDelete, false));
                            int choose = 0;
                            for (Post post1 : postDao.selectList(new LambdaQueryWrapper<Post>().eq(Post::getProjectId, post.getProjectId())
                                    .eq(Post::getIsDelete, false))) {
                                JSONObject jsonObject1 = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(post1.getAnswer()));
                                if (jsonObject1.containsKey(s) && jsonObject1.getJSONArray(s).contains(possibleAnswer)) {
                                    choose++;
                                }
                            }
                            answerMap.put(possibleAnswer, (choose/(double)total));
                            if (total == 0) {
                                answerMap.put(possibleAnswer, 0.0);
                            }
                            possibleAnswersList.add(answerMap);
                        }
                    } else {
                        Map<String, Double> answerMap = new HashMap<>();
                        answerMap.put(answerDTO.getPossibleAnswers(), 0.0);
                        possibleAnswersList.add(answerMap);
                    }
                    answerDTO.setPossibleAnswerList(possibleAnswersList);
                }
                // 转换正确答案格式
                if (Objects.nonNull(answerDTO.getCorrectAnswers())) {
                    answerDTO.setCorrectAnswers(StringEscapeUtils.unescapeHtml4(answerDTO.getCorrectAnswers()));
                    answerDTO.setCorrectAnswerList(CommonUtils.castList(JSON.parseObject(answerDTO.getCorrectAnswers(),
                            List.class), String.class));
                }
                // 判断是否正确
                answerDTO.setIsCorrect(true);
                if (answerDTO.getCorrectAnswers() != null &&
                        !new TreeSet<>(myAnswers).equals(new TreeSet<>(answerDTO.getCorrectAnswerList()))) {
                    answerDTO.setIsCorrect(false);
                }
                // 转换我的答案格式
                answerDTO.setMyAnswers(myAnswers);
                questionPostDTO.setAnswerDTO(answerDTO);
            }
            questionPostDTOS.add(questionPostDTO);
        }
        return questionPostDTOS;
    }
}
