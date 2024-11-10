package com.example.training.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.dao.RoleResourceDao;
import com.example.training.entity.RoleResource;
import com.example.training.service.RoleResourceService;
import org.springframework.stereotype.Service;

/**
 * @author Xuxinyuan
 */
@Service
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceDao, RoleResource> implements RoleResourceService {
}
