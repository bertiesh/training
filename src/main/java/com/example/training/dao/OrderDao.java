package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.dto.OrderDTO;
import com.example.training.entity.Order;
import com.example.training.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xuxinyuan
 */
@Repository
public interface OrderDao extends BaseMapper<Order> {
    /**
     * get orders
     *
     * @param current   page
     * @param size      size
     * @param condition condition
     * @return order list
     */
    List<OrderDTO> listOrders(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

    /**
     * get order count
     *
     * @param condition condition
     * @return order count
     */
    Integer countOrders(@Param("condition") ConditionVO condition);
}
