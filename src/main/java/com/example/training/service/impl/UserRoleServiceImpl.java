package com.example.training.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.dao.UserRoleDao;
import com.example.training.entity.UserRole;
import com.example.training.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * @author Xuxinyuan
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRole> implements UserRoleService {
}
