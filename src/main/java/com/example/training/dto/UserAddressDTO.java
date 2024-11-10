package com.example.training.dto;

import com.example.training.entity.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 收件人姓名
     */
    private String receiverName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮编
     */
    private String postalCode;

    /**
     * 地区编码
     */
    private String regionCode;

    /**
     * 地区
     */
    private Region region;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 是否默认
     */
    private Boolean isDefault;
}
