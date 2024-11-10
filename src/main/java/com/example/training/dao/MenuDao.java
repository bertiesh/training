package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.entity.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface MenuDao extends BaseMapper<Menu> {
    /**
     * get menu by user id
     * @param userInfoId user info id
     * @return menu list
     */
    List<Menu> listMenusByUserInfoId(Integer userInfoId);
}
