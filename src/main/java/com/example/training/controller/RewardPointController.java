package com.example.training.controller;

import com.example.training.annotation.OptLog;
import com.example.training.dto.*;
import com.example.training.entity.Freight;
import com.example.training.entity.ShoppingCart;
import com.example.training.entity.UserAddress;
import com.example.training.service.RewardPointService;
import com.example.training.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.training.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * @author Xinyuan Xu
 */
@Api(tags = "积分模块")
@RestController
public class RewardPointController {
    @Autowired
    private RewardPointService rewardPointService;

    /**
     * get reward list
     *
     * @return {@link Result < RewardPointDTO >} reward list
     */
    @ApiOperation(value = "查看积分列表")
    @GetMapping("/rewardPoints")
    public Result<List<RewardPointDTO>> listRewardPoints() {
        return Result.ok(rewardPointService.listRewardPoints());
    }

    /**
     * get backend reward list
     *
     * @param conditionVO condition
     * @return {@link Result < RewardPointBackDTO >} reward list
     */
    @ApiOperation(value = "查看积分列表")
    @GetMapping("/admin/rewardPoints")
    public Result<PageResult<RewardPointBackDTO>> listRewardPointsBack(ConditionVO conditionVO) {
        return Result.ok(rewardPointService.listRewardPointsBack(conditionVO));
    }

    /**
     * reward compensation
     * @param rewardVO reward info
     * @return {@link Result <>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改积分信息")
    @PostMapping("/admin/rewardPoints")
    public Result<?> saveOrUpdatePoints(@Valid @RequestBody RewardPointVO rewardVO) {
        rewardPointService.saveOrUpdatePoints(rewardVO);
        return Result.ok();
    }

    /**
     * add/update user address
     *
     * @param userAddressVO user address
     * @return {@link Result <>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改用户地址")
    @PostMapping("/address")
    public Result<?> saveOrUpdateAddress(@Valid @RequestBody UserAddressVO userAddressVO) {
        rewardPointService.saveOrUpdateAddress(userAddressVO);
        return Result.ok();
    }

    /**
     * get user address
     *
     * @param conditionVO condition
     * @return {@link Result <UserAddressDTO>}
     */
    @ApiOperation(value = "查看用户地址")
    @GetMapping("/address")
    public Result<List<UserAddressDTO>> listAddress(ConditionVO conditionVO) {
        return Result.ok(rewardPointService.listAddress(conditionVO));
    }

    /**
     * search backend user address
     *
     * @param conditionVO condition
     * @return {@link Result <UserAddress>}
     */
    @ApiOperation(value = "管理员查看用户地址")
    @GetMapping("/admin/address")
    public Result<PageResult<UserAddressBackDTO>> listAddressBack(ConditionVO conditionVO) {
        return Result.ok(rewardPointService.listAddressBack(conditionVO));
    }

    /**
     * add/update spu
     *
     * @param spuVO spu
     * @return {@link Result <>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改spu")
    @PostMapping("/admin/spu")
    public Result<?> saveOrUpdateSPU(@Valid @RequestBody SPUVO spuVO) {
        rewardPointService.saveOrUpdateSPU(spuVO);
        return Result.ok();
    }

    /**
     * search backend spu
     *
     * @param conditionVO condition
     * @return {@link Result <SPUBackDTO>}
     */
    @ApiOperation(value = "后台查看搜索spu")
    @GetMapping("/admin/spu")
    public Result<PageResult<SPUBackDTO>> listSPUBacks(ConditionVO conditionVO) {
        return Result.ok(rewardPointService.listSPUBacks(conditionVO));
    }

    /**
     * add/update sku
     *
     * @param skuVO sku
     * @return {@link Result <>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改sku")
    @PostMapping("/admin/sku")
    public Result<?> saveOrUpdateSKU(@Valid @RequestBody SKUVO skuVO) {
        rewardPointService.saveOrUpdateSKU(skuVO);
        return Result.ok();
    }

    /**
     * search spu
     *
     * @param conditionVO condition
     * @return {@link Result <SPUHomeDTO>}
     */
    @ApiOperation(value = "查看搜索spu")
    @GetMapping("/spu")
    public Result<PageResult<SPUHomeDTO>> listSPUs(ConditionVO conditionVO) {
        return Result.ok(rewardPointService.listSPUs(conditionVO));
    }

    /**
     * get spu by id
     *
     * @param spuId spu id
     * @return {@link Result<SPUDTO>} spu
     */
    @ApiOperation(value = "根据id查看后台spu")
    @ApiImplicitParam(name = "spuId", value = "spu id", required = true, dataType = "Integer")
    @GetMapping("/spu/{spuId}")
    public Result<SPUDTO> getSPUById(@PathVariable("spuId") Integer spuId) {
        return Result.ok(rewardPointService.getSPUById(spuId));
    }

    /**
     * add/update cart
     *
     * @param shoppingCartVO cart
     * @return {@link Result <>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改购物车")
    @PostMapping("/shoppingCart")
    public Result<?> saveOrUpdateShoppingCart(@Valid @RequestBody ShoppingCartVO shoppingCartVO) {
        rewardPointService.saveOrUpdateShoppingCart(shoppingCartVO);
        return Result.ok();
    }

    /**
     * get cart by id
     *
     * @param cartIds cart id
     * @return {@link Result<ShoppingCartDTO>} cart list
     */
    @ApiOperation(value = "根据id查看购物车")
    @ApiImplicitParam(name = "cartIds", value = "购物车id", required = true, dataType = "String")
    @GetMapping("/shoppingCart/{cartIds}")
    public Result<List<ShoppingCartDTO>> getShoppingCartByIds(@PathVariable("cartIds") String cartIds) {
        return Result.ok(rewardPointService.getShoppingCartByIds(cartIds));
    }

    /**
     * search cart
     *
     * @param conditionVO condition
     * @return {@link Result <ShoppingCartDTO>}
     */
    @ApiOperation(value = "查看搜索购物车")
    @GetMapping("/shoppingCart")
    public Result<PageResult<ShoppingCartDTO>> listShoppingCart(ConditionVO conditionVO) {
        return Result.ok(rewardPointService.listShoppingCart(conditionVO));
    }

    /**
     * search backend cart
     *
     * @param conditionVO condition
     * @return {@link Result <ShoppingCartBackDTO>}
     */
    @ApiOperation(value = "管理员查看搜索购物车")
    @GetMapping("/admin/shoppingCart")
    public Result<PageResult<ShoppingCartBackDTO>> listShoppingCartBack(ConditionVO conditionVO) {
        return Result.ok(rewardPointService.listShoppingCartBack(conditionVO));
    }

    /**
     * add/update order
     *
     * @param orderVO order
     * @return {@link Result <>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改订单")
    @PostMapping("/order")
    public Result<?> saveOrUpdateOrder(@Valid @RequestBody OrderVO orderVO) {
        rewardPointService.saveOrUpdateOrder(orderVO);
        return Result.ok();
    }

    /**
     * admin update order
     *
     * @param orderBackVO order
     * @return {@link Result <>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改订单")
    @PostMapping("/admin/order")
    public Result<?> updateOrder(@Valid @RequestBody OrderBackVO orderBackVO) {
        rewardPointService.updateOrder(orderBackVO);
        return Result.ok();
    }

    /**
     * search order
     *
     * @param conditionVO condition
     * @return {@link Result <OrderDTO>}
     */
    @ApiOperation(value = "查看搜索订单")
    @GetMapping("/order")
    public Result<PageResult<OrderDTO>> listOrders(ConditionVO conditionVO) {
        return Result.ok(rewardPointService.listOrders(conditionVO));
    }

    /**
     * admin search order
     *
     * @param conditionVO condition
     * @return {@link Result <OrderDTO>}
     */
    @ApiOperation(value = "管理员查看搜索订单")
    @GetMapping("/admin/order")
    public Result<PageResult<OrderDTO>> listOrdersBacks(ConditionVO conditionVO) {
        return Result.ok(rewardPointService.listOrdersBacks(conditionVO));
    }

    /**
     * get freight
     *
     * @param conditionVO condition
     * @return {@link Result <FreightBackDTO>}
     */
    @ApiOperation(value = "查看搜索物流")
    @GetMapping("/admin/freight")
    public Result<PageResult<FreightBackDTO>> listFreightBacks(ConditionVO conditionVO) {
        return Result.ok(rewardPointService.listFreightBacks(conditionVO));
    }

    /**
     * cancel order
     *
     * @param orderId order id
     * @return {@link Result <>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "取消订单")
    @GetMapping("/order/{orderId}")
    public Result<?> cancelOrder(@PathVariable("orderId") Integer orderId) {
        rewardPointService.cancelOrder(orderId);
        return Result.ok();
    }
}
