package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.entity.OperationLog;
import org.springframework.stereotype.Repository;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface OperationLogDao extends BaseMapper<OperationLog> {
}
