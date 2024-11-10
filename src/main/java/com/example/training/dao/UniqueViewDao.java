package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.dto.UniqueViewDTO;
import com.example.training.entity.UniqueView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface UniqueViewDao extends BaseMapper<UniqueView> {
    /**
     * Get 7 days of users
     *
     * @param startTime start time
     * @param endTime   end time
     * @return 用户量 number of users
     */
    List<UniqueViewDTO> listUniqueViews(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
