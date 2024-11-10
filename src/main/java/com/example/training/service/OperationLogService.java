package com.example.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.training.dto.OperationLogDTO;
import com.example.training.entity.OperationLog;
import com.example.training.vo.ConditionVO;
import com.example.training.vo.PageResult;

/**
     * @author Xuxinyuan
 */
public interface OperationLogService extends IService<OperationLog> {
    /**
     * 查询日志列表
     *
     * @param conditionVO 条件
     * @return 日志列表
     */
    PageResult<OperationLogDTO> listOperationLogs(ConditionVO conditionVO);
}
