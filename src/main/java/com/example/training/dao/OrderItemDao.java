package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.entity.OrderItem;
import org.springframework.stereotype.Repository;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface OrderItemDao extends BaseMapper<OrderItem> {
}
