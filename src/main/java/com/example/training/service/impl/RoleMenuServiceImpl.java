package com.example.training.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.dao.RoleDao;
import com.example.training.dao.RoleMenuDao;
import com.example.training.entity.Role;
import com.example.training.entity.RoleMenu;
import com.example.training.entity.UserInfo;
import com.example.training.entity.UserRole;
import com.example.training.service.RoleMenuService;
import com.example.training.vo.MenuVO;
import com.example.training.vo.RoleMenuVO;
import com.example.training.vo.UserRoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Xuxinyuan
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuDao, RoleMenu> implements RoleMenuService {
}
