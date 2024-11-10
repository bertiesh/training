package com.example.training.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Xuxinyuan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "文章审核")
public class ArticleTopVO {
    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Integer id;

    /**
     * 审核状态
     */
    @NotNull(message = "审核状态不能为空")
    private Integer isReview;
}
