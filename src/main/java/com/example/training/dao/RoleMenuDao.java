package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.entity.Role;
import com.example.training.entity.RoleMenu;
import org.springframework.stereotype.Repository;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface RoleMenuDao extends BaseMapper<RoleMenu> {
}
