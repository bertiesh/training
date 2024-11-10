package com.example.training.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.dao.QuestionTagDao;
import com.example.training.entity.QuestionTag;
import com.example.training.service.QuestionTagService;
import org.springframework.stereotype.Service;

/**
 * @author Xuxinyuan
 */
@Service
public class QuestionTagServiceImpl extends ServiceImpl<QuestionTagDao, QuestionTag> implements QuestionTagService {
}
