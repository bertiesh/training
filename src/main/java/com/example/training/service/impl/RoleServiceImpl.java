package com.example.training.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.constant.CommonConst;
import com.example.training.dao.MenuDao;
import com.example.training.dao.ResourceDao;
import com.example.training.dao.RoleDao;
import com.example.training.dao.UserRoleDao;
import com.example.training.dto.LabelOptionDTO;
import com.example.training.dto.RoleDTO;
import com.example.training.dto.UserRoleDTO;
import com.example.training.entity.*;
import com.example.training.exception.BizException;
import com.example.training.handler.FilterInvocationSecurityMetadataSourceImpl;
import com.example.training.service.ResourceService;
import com.example.training.service.RoleMenuService;
import com.example.training.service.RoleResourceService;
import com.example.training.service.RoleService;
import com.example.training.util.BeanCopyUtils;
import com.example.training.util.PageUtils;
import com.example.training.vo.ConditionVO;
import com.example.training.vo.PageResult;
import com.example.training.vo.RoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Xuxinyuan
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private ResourceDao resourceDao;
    @Autowired
    private RoleResourceService roleResourceService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private MenuServiceImpl menuService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;

    @Override
    public List<UserRoleDTO> listUserRoles() {
        // 查询角色列表
        List<Role> roleList = roleDao.selectList(new LambdaQueryWrapper<Role>()
                .select(Role::getId, Role::getRoleName, Role::getRoleLabel, Role::getIsDisable));
        return BeanCopyUtils.copyList(roleList, UserRoleDTO.class);
    }

    @Override
    public PageResult<RoleDTO> listRoles(ConditionVO conditionVO) {
        // 查询角色列表
        List<RoleDTO> roleDTOList = roleDao.listRoles(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVO);
        for (RoleDTO roleDTO : roleDTOList) {
            List<Menu> menus = new ArrayList<>();
            for (Integer integer : roleDTO.getMenuIdList()) {
                menus.add(menuDao.selectById(integer));
            }
            List<Resource> resources = new ArrayList<>();
            for (Integer integer : roleDTO.getResourceIdList()) {
                resources.add(resourceDao.selectById(integer));
            }
            // 获取目录列表
            List<Menu> catalogList = menuService.listCatalog(menus);
            List<Resource> catalogList1 = resources.stream().filter(item -> Objects.isNull(item.getParentId()))
                    .collect(Collectors.toList());
            // 获取目录下的子菜单
            Map<Integer, List<Menu>> childrenMap = menuService.getMenuMap(menus);
            Map<Integer, List<Resource>> childrenMap1 = resources.stream()
                    .filter(item -> Objects.nonNull(item.getParentId()))
                    .collect(Collectors.groupingBy(Resource::getParentId));
            // 组装目录菜单数据
            List<LabelOptionDTO> labelOptionDTOS = catalogList.stream().map(item -> {
                // 获取目录下的菜单排序
                List<LabelOptionDTO> list = new ArrayList<>();
                List<Menu> children = childrenMap.get(item.getId());
                if (CollectionUtils.isNotEmpty(children)) {
                    list = children.stream()
                            .sorted(Comparator.comparing(Menu::getOrderNum))
                            .map(menu -> LabelOptionDTO.builder()
                                    .id(menu.getId())
                                    .label(menu.getName())
                                    .build())
                            .collect(Collectors.toList());
                }
                return LabelOptionDTO.builder()
                        .id(item.getId())
                        .label(item.getName())
                        .children(list)
                        .build();
            }).collect(Collectors.toList());
            List<LabelOptionDTO> resourceOptionDTOS = catalogList1.stream().map(item -> {
                List<LabelOptionDTO> list = new ArrayList<>();
                List<Resource> children = childrenMap1.get(item.getId());
                if (CollectionUtils.isNotEmpty(children)) {
                    list = children.stream()
                            .map(resource -> LabelOptionDTO.builder()
                                    .id(resource.getId())
                                    .label(resource.getResourceName())
                                    .build())
                            .collect(Collectors.toList());
                }
                return LabelOptionDTO.builder()
                        .id(item.getId())
                        .label(item.getResourceName())
                        .children(list)
                        .build();
            }).collect(Collectors.toList());
            roleDTO.setLabelOptionDTOS(labelOptionDTOS);
            roleDTO.setResourceOptionDTOS(resourceOptionDTOS);
        }
        // 查询总量
        Integer count = Math.toIntExact(roleDao.selectCount(new LambdaQueryWrapper<Role>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), Role::getRoleName, conditionVO.getKeywords())));
        return new PageResult<>(roleDTOList, count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateRole(RoleVO roleVO) {
        // 判断角色名重复
        Role existRole = roleDao.selectOne(new LambdaQueryWrapper<Role>()
                .select(Role::getId)
                .eq(Role::getRoleName, roleVO.getRoleName()));
        if (Objects.nonNull(existRole) && !existRole.getId().equals(roleVO.getId())) {
            throw new BizException("角色名已存在");
        }
        // 保存或更新角色信息
        Role role = Role.builder()
                .id(roleVO.getId())
                .roleName(roleVO.getRoleName())
                .roleLabel(roleVO.getRoleLabel())
                .isDisable(roleVO.getIsDisable())
                .build();
        this.saveOrUpdate(role);
        // 更新角色资源关系
        if (Objects.nonNull(roleVO.getResourceIdList())) {
            if (Objects.nonNull(roleVO.getId())) {
                roleResourceService.remove(new LambdaQueryWrapper<RoleResource>()
                        .eq(RoleResource::getRoleId, roleVO.getId()));
            }
            List<RoleResource> roleResourceList = roleVO.getResourceIdList().stream()
                    .map(resourceId -> RoleResource.builder()
                            .roleId(role.getId())
                            .resourceId(resourceId)
                            .build())
                    .collect(Collectors.toList());
            roleResourceService.saveBatch(roleResourceList);
            // 重新加载角色资源信息
            filterInvocationSecurityMetadataSource.clearDataSource();
        }
        // 更新角色菜单关系
        if (Objects.nonNull(roleVO.getMenuIdList())) {
            if (Objects.nonNull(roleVO.getId())) {
                roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleVO.getId()));
            }
            List<RoleMenu> roleMenuList = roleVO.getMenuIdList().stream()
                    .map(menuId -> RoleMenu.builder()
                            .roleId(role.getId())
                            .menuId(menuId)
                            .build())
                    .collect(Collectors.toList());
            roleMenuService.saveBatch(roleMenuList);
        }
    }

    @Override
    public void deleteRoles(List<Integer> roleIdList) {
        // 判断角色下是否有用户
        int count = Math.toIntExact(userRoleDao.selectCount(new LambdaQueryWrapper<UserRole>()
                .in(UserRole::getRoleId, roleIdList)));
        if (count > 0) {
            throw new BizException("该角色下存在用户");
        }
        roleDao.deleteBatchIds(roleIdList);
    }
}
