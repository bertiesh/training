package com.example.training.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xuxinyuan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "用户地址")
public class UserAddressVO {
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "id", dataType = "Integer")
    private Integer id;

    /**
     * 收件人姓名
     */
    @ApiModelProperty(name = "receiverName", value = "收件人姓名", dataType = "String")
    private String receiverName;

    /**
     * 邮编
     */
    @ApiModelProperty(name = "postalCode", value = "邮编", dataType = "String")
    private String postalCode;

    /**
     * 区域编号
     */
    @ApiModelProperty(name = "regionCode", value = "区域编号", dataType = "String")
    private String regionCode;

    /**
     * 详细地址
     */
    @ApiModelProperty(name = "detailAddress", value = "详细地址", dataType = "String")
    private String detailAddress;

    /**
     * 联系电话
     */
    @ApiModelProperty(name = "phone", value = "联系电话", dataType = "String")
    private String phone;

    /**
     * 是否默认
     */
    @ApiModelProperty(name = "isDefault", value = "是否默认", dataType = "Boolean")
    private Boolean isDefault;

    /**
     * 是否删除
     */
    @ApiModelProperty(name = "isDelete", value = "是否删除", dataType = "Boolean")
    private Boolean isDelete;
}
