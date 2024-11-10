package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.dto.ResourceRoleDTO;
import com.example.training.dto.RoleDTO;
import com.example.training.entity.Role;
import com.example.training.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface RoleDao extends BaseMapper<Role> {
    /**
     * get routing roles
     *
     * @return role tags
     */
    List<ResourceRoleDTO> listResourceRoles();

    /**
     * get roles by user id
     *
     * @param userInfoId user id
     * @return role tags
     */
    List<String> listRolesByUserInfoId(Integer userInfoId);

    /**
     * get roles
     *
     * @param current     page
     * @param size        size
     * @param conditionVO condition
     * @return roles
     */
    List<RoleDTO> listRoles(@Param("current") Long current, @Param("size") Long size, @Param("conditionVO") ConditionVO conditionVO);
}
