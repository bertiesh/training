package com.example.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.training.dto.RoleDTO;
import com.example.training.dto.UserRoleDTO;
import com.example.training.entity.Role;
import com.example.training.vo.ConditionVO;
import com.example.training.vo.PageResult;
import com.example.training.vo.RoleVO;

import java.util.List;

/**
 * @author Xuxinyuan
 */
public interface RoleService extends IService<Role> {
    /**
     * 获取用户角色选项
     *
     * @return 角色
     */
    List<UserRoleDTO> listUserRoles();

    /**
     * 查询角色列表
     *
     * @param conditionVO 条件
     * @return 角色列表
     */
    PageResult<RoleDTO> listRoles(ConditionVO conditionVO);

    /**
     * 保存或更新角色
     *
     * @param roleVO 角色
     */
    void saveOrUpdateRole(RoleVO roleVO);

    /**
     * 删除角色
     * @param roleIdList 角色id列表
     */
    void deleteRoles(List<Integer> roleIdList);
}
