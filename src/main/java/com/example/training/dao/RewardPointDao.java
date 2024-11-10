package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.entity.RewardPoint;
import com.example.training.util.BeanCopyUtils;
import org.springframework.stereotype.Repository;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface RewardPointDao extends BaseMapper<RewardPoint> {
}
