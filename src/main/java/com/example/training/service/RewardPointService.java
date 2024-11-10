package com.example.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.training.dto.*;
import com.example.training.entity.Freight;
import com.example.training.entity.RewardPoint;
import com.example.training.vo.*;

import java.util.List;

/**
 * @author Xuxinyuan
 */
public interface RewardPointService extends IService<RewardPoint> {
    /**
     * 查看积分列表
     * @return {@link Result < RewardPointDTO >} 积分列表
     */
    List<RewardPointDTO> listRewardPoints();

    /**
     * 查看后台积分列表
     *
     * @param conditionVO 条件信息
     * @return {@link Result < RewardPointBackDTO >} 积分列表
     */
    PageResult<RewardPointBackDTO> listRewardPointsBack(ConditionVO conditionVO);

    /**
     * 积分补偿
     *
     * @param rewardVO 积分信息
     */
    void saveOrUpdatePoints(RewardPointVO rewardVO);

    /**
     * 添加或修改用户地址
     *
     * @param userAddressVO 用户地址
     */
    void saveOrUpdateAddress(UserAddressVO userAddressVO);

    /**
     * 查看用户地址
     *
     * @param conditionVO 条件
     * @return {@link Result <UserAddressDTO>}
     */
    List<UserAddressDTO> listAddress(ConditionVO conditionVO);

    /**
     * 管理员查看用户地址
     *
     * @param conditionVO 条件
     * @return {@link Result <UserAddress>}
     */
    PageResult<UserAddressBackDTO> listAddressBack(ConditionVO conditionVO);

    /**
     * 添加或修改spu
     *
     * @param spuVO spu
     */
    void saveOrUpdateSPU(SPUVO spuVO);

    /**
     * 后台查看搜索spu
     * @param conditionVO 条件
     * @return {@link Result <SPUBackDTO>}
     */
    PageResult<SPUBackDTO> listSPUBacks(ConditionVO conditionVO);

    /**
     * 添加或修改sku
     * @param skuVO sku
     */
    void saveOrUpdateSKU(SKUVO skuVO);

    /**
     * 查看搜索spu
     * @param conditionVO 条件
     * @return {@link Result <SPUHomeDTO>}
     */
    PageResult<SPUHomeDTO> listSPUs(ConditionVO conditionVO);

    /**
     * 根据id查看spu
     * @param spuId spu id
     * @return {@link Result<SPUDTO>} spu
     */
    SPUDTO getSPUById(Integer spuId);

    /**
     * 添加或修改购物车
     *
     * @param shoppingCartVO 购物车
     */
    void saveOrUpdateShoppingCart(ShoppingCartVO shoppingCartVO);

    /**
     * 根据id查看购物车
     * @param cartIds 购物车id
     * @return {@link Result<ShoppingCartDTO>} 购物车列表
     */
    List<ShoppingCartDTO> getShoppingCartByIds(String cartIds);

    /**
     * 查看搜索购物车
     * @param conditionVO 条件
     * @return {@link Result <ShoppingCartDTO>}
     */
    PageResult<ShoppingCartDTO> listShoppingCart(ConditionVO conditionVO);

    /**
     * 管理员查看搜索购物车
     * @param conditionVO 条件
     * @return {@link Result <ShoppingCartBackDTO>}
     */
    PageResult<ShoppingCartBackDTO> listShoppingCartBack(ConditionVO conditionVO);

    /**
     * 添加或修改订单
     *
     * @param orderVO 订单
     */
    void saveOrUpdateOrder(OrderVO orderVO);

    /**
     * 管理员修改订单
     *
     * @param orderBackVO 订单
     */
    void updateOrder(OrderBackVO orderBackVO);

    /**
     * 查看搜索订单
     * @param conditionVO 条件
     * @return {@link Result <OrderDTO>}
     */
    PageResult<OrderDTO> listOrders(ConditionVO conditionVO);

    /**
     * 查看搜索订单
     * @param conditionVO 条件
     * @return {@link Result <OrderDTO>}
     */
    PageResult<OrderDTO> listOrdersBacks(ConditionVO conditionVO);

    /**
     * 查看搜索物流
     *
     * @param conditionVO 条件
     * @return {@link Result <FreightBackDTO>}
     */
    PageResult<FreightBackDTO> listFreightBacks(ConditionVO conditionVO);

    /**
     * 取消订单
     *
     * @param orderId 订单id
     */
    void cancelOrder(Integer orderId);
}
