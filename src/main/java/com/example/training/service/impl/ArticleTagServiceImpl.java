package com.example.training.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.dao.ArticleTagDao;
import com.example.training.entity.ArticleTag;
import com.example.training.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * @author Xuxinyuan
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagDao, ArticleTag> implements ArticleTagService {
}
