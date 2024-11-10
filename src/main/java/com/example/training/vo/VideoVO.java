package com.example.training.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

/**
 * @author Xuxinyuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "视频")
public class VideoVO {
    @NotBlank(message = "视频地址不能为空")
    @ApiModelProperty(name = "videoURI", value = "视频地址", required = true, dataType = "string")
    private String videoURI;

//    @Range(min = 15, message = "超时时间过段，最小15秒")
    @ApiModelProperty(value = "超时时间", dataType = "int", required = true)
    private Integer timeOut;
}
