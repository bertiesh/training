package com.example.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.training.dto.HelpBackDTO;
import com.example.training.dto.HelpDTO;
import com.example.training.entity.Help;
import com.example.training.vo.*;

import java.util.List;

/**
 * @author Xuxinyuan
 */
public interface HelpService extends IService<Help> {
    /**
     * 添加或修改帮助
     *
     * @param helpVO 帮助信息
     */
    void saveOrUpdateHelps(HelpVO helpVO);

    /**
     * 删除帮助
     *
     * @param helpIdList 帮助id列表
     */
    void deleteHelps(List<Integer> helpIdList);

    /**
     * 查看后台帮助
     *
     * @param conditionVO 条件
     * @return {@link Result<HelpBackDTO>} 后台帮助列表
     */
    PageResult<HelpBackDTO> listHelpBacks(ConditionVO conditionVO);

    /**
     * 查看常见问题
     * @param conditionVO 条件
     * @return {@link Result<HelpDTO>} 常见问题列表
     */
    List<HelpDTO> listHelps(ConditionVO conditionVO);

    /**
     * 发送删除消息
     * @param messageVO 帮助信息
     * @return {@link Result <HelpRecord>}
     */
    void saveOrUpdateHelpRecords(MessageVO messageVO);
}
