package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.dto.SPUBackDTO;
import com.example.training.entity.SPU;
import com.example.training.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface SPUDao extends BaseMapper<SPU> {
    /**
     * get backend spu
     *
     * @param current   page
     * @param size      size
     * @param condition condition
     * @return spu
     */
    List<SPUBackDTO> listSPUBacks(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

    /**
     * get backend spu count
     *
     * @param condition condition
     * @return spu count
     */
    Integer countSPUBacks(@Param("condition") ConditionVO condition);
}
