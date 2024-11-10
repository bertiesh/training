package com.example.training.controller;

import com.example.training.annotation.OptLog;
import com.example.training.dto.RoleDTO;
import com.example.training.dto.UserRoleDTO;
import com.example.training.service.RoleService;
import com.example.training.vo.ConditionVO;
import com.example.training.vo.PageResult;
import com.example.training.vo.Result;
import com.example.training.vo.RoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.training.constant.OptTypeConst.REMOVE;
import static com.example.training.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * @author Xinyuan Xu
 */
@Api(tags = "角色模块")
@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * get user role
     *
     * @return {@link Result < UserRoleDTO >} user role option
     */
    @ApiOperation(value = "查询用户角色选项")
    @GetMapping("/admin/users/role")
    public Result<List<UserRoleDTO>> listUserRoles() {
        return Result.ok(roleService.listUserRoles());
    }

    /**
     * get role list
     *
     * @param conditionVO condition
     * @return {@link Result<RoleDTO>} role list
     */
    @ApiOperation(value = "查询角色列表")
    @GetMapping("/admin/roles")
    public Result<PageResult<RoleDTO>> listRoles(ConditionVO conditionVO) {
        return Result.ok(roleService.listRoles(conditionVO));
    }

    /**
     * save/update role
     *
     * @param roleVO role info
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新角色")
    @PostMapping("/admin/role")
    public Result<?> saveOrUpdateRole(@RequestBody @Valid RoleVO roleVO) {
        roleService.saveOrUpdateRole(roleVO);
        return Result.ok();
    }

    /**
     * delete role
     *
     * @param roleIdList role id list
     * @return {@link Result<>}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除角色")
    @DeleteMapping("/admin/roles")
    public Result<?> deleteRoles(@RequestBody List<Integer> roleIdList) {
        roleService.deleteRoles(roleIdList);
        return Result.ok();
    }
}
