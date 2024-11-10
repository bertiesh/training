package com.example.training.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.dao.FollowDao;
import com.example.training.entity.Follow;
import com.example.training.service.FollowService;
import org.springframework.stereotype.Service;

/**
 * @author Xuxinyuan
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowDao, Follow> implements FollowService {
}
