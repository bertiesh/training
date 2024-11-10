package com.example.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.training.dto.SPUBackDTO;
import com.example.training.dto.ShoppingCartBackDTO;
import com.example.training.dto.ShoppingCartDTO;
import com.example.training.entity.ShoppingCart;
import com.example.training.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Repository
public interface ShoppingCartDao extends BaseMapper<ShoppingCart> {
    /**
     * get cart
     *
     * @param current   page
     * @param size      size
     * @param condition condition
     * @return cart
     */
    List<ShoppingCartBackDTO> listShoppingCartBacks(@Param("current") Long current, @Param("size") Long size,
                                                    @Param("condition") ConditionVO condition);

    /**
     * get cart
     *
     * @param current   page
     * @param size      size
     * @param condition condition
     * @return cart
     */
    List<ShoppingCartDTO> listShoppingCart(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

    /**
     * get cart count
     *
     * @param condition condition
     * @return cart count
     */
    Integer countShoppingCart(@Param("condition") ConditionVO condition);
}
