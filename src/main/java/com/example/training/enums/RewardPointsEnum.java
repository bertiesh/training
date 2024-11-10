package com.example.training.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Xuxinyuan
 */

@Getter
@AllArgsConstructor
public enum RewardPointsEnum {
    /**
     * 发表单篇文章积分
     */
    ARTICLE_REWARD(1, 1),

    /**
     * 完成问卷积分
     */
    SURVEY_REWARD(2, 1);

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 描述
     */
    private final Integer point;

    /**
     * 根据类型获取枚举
     * @param type 类型
     * @return 枚举
     */
    public static RewardPointsEnum rewardPointsEnum(Integer type) {
        for (RewardPointsEnum rewardPoints : RewardPointsEnum.values()) {
            if (rewardPoints.getType().equals(type)) {
                return rewardPoints;
            }
        }
        return null;
    }
}
