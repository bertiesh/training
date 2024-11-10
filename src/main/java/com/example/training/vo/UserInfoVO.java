package com.example.training.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Xuxinyuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户信息对象")
public class UserInfoVO {
    /**
     * 用户昵称
     */
    @NotBlank(message = "昵称不能为空")
    @ApiModelProperty(name = "nickname", value = "昵称", dataType = "String")
    private String nickname;

    /**
     * 用户简介
     */
    @ApiModelProperty(name = "intro", value = "介绍", dataType = "String")
    private String intro;

    /**
     * 手机号
     */
    @ApiModelProperty(name = "phone", value = "手机号", dataType = "String")
    private String phone;

    /**
     * 医院
     */
    @ApiModelProperty(name = "hospital", value = "医院", dataType = "String")
    private String hospital;

    /**
     * 职称等级
     */
    @ApiModelProperty(name = "qualifications", value = "职称等级", dataType = "String")
    private String qualifications;

    /**
     * 新增关注id
     */
    @ApiModelProperty(name = "followIdList", value = "用户列表", required = true, dataType = "List<Integer>")
    private List<Integer> followIdList;

    /**
     * 关注列表
     */
    @ApiModelProperty(name = "userIdList", value = "用户列表", required = true, dataType = "List<Integer>")
    private List<Integer> userIdList;
}
