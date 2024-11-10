package com.example.training.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Xuxinyuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "角色菜单权限")
public class RoleMenuVO {
    /**
     * 角色id
     */
    @NotNull(message = "id不能为空")
    @ApiModelProperty(name = "roleId", value = "角色id", dataType = "Integer")
    private Integer roleId;
    /**
     * 角色菜单
     */
    @NotNull(message = "角色菜单不能为空")
    @ApiModelProperty(name = "menuList", value = "菜单id集合", dataType = "List<Integer>")
    private List<Integer> menuIdList;
}
