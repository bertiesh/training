package com.example.training.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.dao.AnswerDao;
import com.example.training.entity.Answer;
import com.example.training.service.AnswerService;
import org.springframework.stereotype.Service;

/**
 * @author Xuxinyuan
 */
@Service
public class AnswerServiceImpl extends ServiceImpl<AnswerDao, Answer> implements AnswerService {
}
