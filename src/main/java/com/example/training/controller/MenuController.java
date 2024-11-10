package com.example.training.controller;

import com.example.training.annotation.OptLog;
import com.example.training.dto.LabelOptionDTO;
import com.example.training.dto.MenuDTO;
import com.example.training.dto.UserMenuDTO;
import com.example.training.service.MenuService;
import com.example.training.service.RoleMenuService;
import com.example.training.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.baomidou.mybatisplus.core.assist.ISqlRunner.UPDATE;

/**
 * @author Xuxinyuan
 */
@Api(tags = "菜单模块")
@RestController
public class MenuController {
    @Autowired
    private MenuService menuService;

    /**
     * search menu list
     *
     * @param conditionVO condition
     * @return {@link Result <MenuDTO>} menu list
     */
    @ApiOperation(value = "查看菜单列表")
    @GetMapping("/admin/menus")
    public Result<List<MenuDTO>> listMenus(ConditionVO conditionVO) {
        return Result.ok(menuService.listMenus(conditionVO));
    }

    /**
     * add/update menu
     *
     * @param menuVO menu
     * @return {@link Result<>}
     */
    @ApiOperation(value = "新增或修改菜单")
    @PostMapping("/admin/menus")
    public Result<?> saveOrUpdateMenu(@Valid @RequestBody MenuVO menuVO) {
        menuService.saveOrUpdateMenu(menuVO);
        return Result.ok();
    }

    /**
     * delete menu
     *
     * @param menuId menu id
     * @return {@link Result<>}
     */
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("/admin/menus/{menuId}")
    public Result<?> deleteMenu(@PathVariable("menuId") Integer menuId){
        menuService.deleteMenu(menuId);
        return Result.ok();
    }

    /**
     * get role menu
     *
     * @return {@link Result< LabelOptionDTO >} role menu
     */
    @ApiOperation(value = "查看角色菜单选项")
    @GetMapping("/admin/role/menus")
    public Result<List<LabelOptionDTO>> listMenuOptions() {
        return Result.ok(menuService.listMenuOptions());
    }

    /**
     * current user menu
     *
     * @return {@link Result<UserMenuDTO>} menu list
     */
    @ApiOperation(value = "查看当前用户菜单")
    @GetMapping("/admin/user/menus")
    public Result<List<UserMenuDTO>> listUserMenus() {
        return Result.ok(menuService.listUserMenus());
    }

    /**
     * update role menu
     *
     * @param roleMenuVO role menu info
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改角色菜单")
    @PutMapping("/admin/roles/menus")
    public Result<?> updateUserRole(@Valid @RequestBody RoleMenuVO roleMenuVO) {
        menuService.updateRoleMenu(roleMenuVO);
        return Result.ok();
    }
}
