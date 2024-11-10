package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.dto.HelpBackDTO;
import com.example.training.dto.QuestionBackDTO;
import com.example.training.entity.Help;
import com.example.training.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface HelpDao extends BaseMapper<Help> {
    /**
     * get backend help
     *
     * @param current   page
     * @param size      size
     * @param condition condition
     * @return help list
     */
    List<HelpBackDTO> listHelpBacks(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

    /**
     * get backend help count
     *
     * @param condition condition
     * @return help count
     */
    Integer countHelpBacks(@Param("condition") ConditionVO condition);
}
