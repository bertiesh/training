package com.example.training.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.dao.PostDao;
import com.example.training.entity.Post;
import com.example.training.service.PostService;
import org.springframework.stereotype.Service;

/**
 * @author Xuxinyuan
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostDao, Post> implements PostService {
}
