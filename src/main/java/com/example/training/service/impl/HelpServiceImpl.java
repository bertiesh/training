package com.example.training.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.dao.HelpDao;
import com.example.training.dao.UserInfoDao;
import com.example.training.dto.*;
import com.example.training.entity.Help;
import com.example.training.entity.Message;
import com.example.training.service.HelpService;
import com.example.training.service.MessageService;
import com.example.training.util.*;
import com.example.training.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.training.util.FormatUtil.maxSimilarity;

/**
 * @author Xuxinyuan
 */
@Service
public class HelpServiceImpl extends ServiceImpl<HelpDao, Help> implements HelpService {
    @Autowired
    private HelpDao helpDao;
    @Autowired
    private MessageService messageService;
    @Resource
    private HttpServletRequest request;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateHelps(HelpVO helpVO) {
        // 保存或修改题目
        Help help = putFeatures(helpVO);
        this.saveOrUpdate(help);
    }

    private Help putFeatures(HelpVO helpVO) {
        Help help = BeanCopyUtils.copyObject(helpVO, Help.class);
        if (Objects.nonNull(helpVO.getQuestion())) {
            List<String> featureWords = FormatUtil.RemovalOfStopWords(helpVO.getQuestion());
            help.setStandardQuestionFeatures(JSONUtil.toJsonStr(featureWords));
        }
        if (Objects.nonNull(helpVO.getSynonymicQuestions())){
            List<Integer> synQs = Arrays.stream(helpVO.getSynonymicQuestions().split(","))
                    .map(Integer::parseInt).collect(Collectors.toList());
            List<String> synQsFeatures = new ArrayList<>();
            for (Integer synQ : synQs) {
                if (Objects.nonNull(helpDao.selectById(synQ).getStandardQuestionFeatures())) {
                    synQsFeatures.addAll(CommonUtils.castList(JSON.parseObject(helpDao.selectById(synQ).
                                    getStandardQuestionFeatures(), List.class), String.class));
                }
            }
            synQsFeatures = synQsFeatures.stream().distinct().collect(Collectors.toList());
            help.setSynonymicQuestionsFeatures(JSONUtil.toJsonStr(synQsFeatures));
        }
        return help;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteHelps(List<Integer> helpIdList) {
        // 删除题目
        helpDao.deleteBatchIds(helpIdList);
    }

    @Override
    public PageResult<HelpBackDTO> listHelpBacks(ConditionVO condition) {
        // 查询题目总量
        Integer count = helpDao.countHelpBacks(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台题目
        List<HelpBackDTO> helpBackDTOList = helpDao.listHelpBacks(PageUtils.getLimitCurrent(),
                PageUtils.getSize(), condition);
        helpBackDTOList.forEach(item -> {
            // 转换题目关键词格式
            if (Objects.nonNull(item.getStandardQuestionFeatures())) {
                item.setStandardQuestionFeaturesList(CommonUtils.castList(JSON.parseObject(
                        item.getStandardQuestionFeatures(), List.class), String.class));
            }
            // 转换关联题目关键词格式
            if (Objects.nonNull(item.getSynonymicQuestionsFeatures())) {
                item.setSynonymicQuestionsFeaturesList(CommonUtils.castList(JSON.parseObject(
                        item.getSynonymicQuestionsFeatures(), List.class), String.class));
            }
        });
        return new PageResult<>(helpBackDTOList, count);
    }

    @Override
    public List<HelpDTO> listHelps(ConditionVO conditionVO) {
        return BeanCopyUtils.copyList(helpDao.selectList(new LambdaQueryWrapper<Help>()
                .eq(Help::getStatus, true).eq(Help::getIsDelete, false)
                .like(Objects.nonNull(conditionVO.getKeywords()), Help::getQuestion, conditionVO.getKeywords())), HelpDTO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateHelpRecords(MessageVO messageVO) {
        Message message = BeanCopyUtils.copyObject(messageVO, Message.class);
        String ipAddress = IpUtils.getIpAddress(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        message.setFromId(UserUtils.getLoginUser().getUserInfoId());
        message.setToId(1);
        message.setStatus(true);
        message.setConversationCode("小管家");
        message.setMessageContent(HTMLUtils.filter(message.getMessageContent()));
        message.setIpAddress(ipAddress);
        message.setIpSource(ipSource);
        messageService.saveOrUpdate(message);
        String content = "不好意思，我还没有学会这个问题，您可以换个问题问我";
        Map<Object,List<String>> standardQuestionContent = new HashMap<>();
        for (Help help : helpDao.selectList(new LambdaQueryWrapper<Help>().eq(Help::getIsDelete, false))) {
            List<String> list = CommonUtils.castList(JSON.parseObject(help.getStandardQuestionFeatures(), List.class),
                    String.class);
            list.addAll(CommonUtils.castList(JSON.parseObject(help.getSynonymicQuestionsFeatures(), List.class),
                    String.class));
            list = list.stream().distinct().collect(Collectors.toList());
            standardQuestionContent.put(help.getId(), list);
        }
        Map<String, Object> map= maxSimilarity(message.getMessageContent(), standardQuestionContent);
        String id = (String)map.get("id");
        Float max = (Float) map.get("max");
        if (Objects.nonNull(id)){
            for (Help help : helpDao.selectList(new LambdaQueryWrapper<Help>().eq(Help::getIsDelete, false))) {
                if (help.getId().equals(Integer.parseInt(id))){
                    content = help.getAnswer();
                    break;
                }
            }
        }
        if (max < 0.19) {
            content = "不好意思，我还没有学会这个问题，您可以换个问题问我";
        }
        Message reply = new Message();
        reply.setToId(UserUtils.getLoginUser().getUserInfoId());
        reply.setFromId(1);
        reply.setStatus(true);
        reply.setConversationCode("小管家");
        reply.setMessageContent(content);
        reply.setIpAddress(ipAddress);
        reply.setIpSource(ipSource);
        messageService.saveOrUpdate(reply);
    }
}
