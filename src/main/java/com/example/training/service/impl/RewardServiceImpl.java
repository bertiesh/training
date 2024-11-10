package com.example.training.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.constant.CommonConst;
import com.example.training.constant.RedisPrefixConst;
import com.example.training.dao.*;
import com.example.training.dto.*;
import com.example.training.entity.*;
import com.example.training.exception.BizException;
import com.example.training.service.RedisService;
import com.example.training.service.RewardPointService;
import com.example.training.util.*;
import com.example.training.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Xuxinyuan
 */
@Service
public class RewardServiceImpl extends ServiceImpl<RewardPointDao, RewardPoint> implements RewardPointService {
    @Autowired
    private RewardPointDao rewardPointDao;
    @Autowired
    private UserAddressDao userAddressDao;
    @Autowired
    private RegionDao regionDao;
    @Autowired
    private SPUDao spuDao;
    @Autowired
    private SKUDao skuDao;
    @Autowired
    private ShoppingCartDao shoppingCartDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private FreightDao freightDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private UserAddressServiceImpl userAddressServiceImpl;
    @Autowired
    private SPUServiceImpl spUServiceImpl;
    @Autowired
    private SKUServiceImpl skuService;
    @Autowired
    private ShoppingCartServiceImpl shoppingCartService;
    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private OrderItemServiceImpl orderItemService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private HttpSession session;

    @Override
    public List<RewardPointDTO> listRewardPoints() {
        List<RewardPointDTO> rewardPoints = new ArrayList<>();
        rewardPointDao.selectList(new LambdaQueryWrapper<RewardPoint>().orderByDesc(RewardPoint::getId)
                .eq(RewardPoint::getUserId, UserUtils.getLoginUser().getUserInfoId())).forEach(rewardPoint -> {
                    rewardPoints.add(BeanCopyUtils.copyObject(rewardPoint, RewardPointDTO.class));
                });
        return rewardPoints;
    }

    @Override
    public PageResult<RewardPointBackDTO> listRewardPointsBack(ConditionVO conditionVO) {
        List<RewardPointBackDTO> rewardPoints = BeanCopyUtils.copyList(rewardPointDao.selectList(
                new LambdaQueryWrapper<RewardPoint>().orderByDesc(RewardPoint::getId)
                .eq(Objects.nonNull(conditionVO.getUserInfoId()), RewardPoint::getUserId, conditionVO.getUserInfoId())
                .eq(Objects.nonNull(conditionVO.getKeywords()), RewardPoint::getEvent, conditionVO.getKeywords())
                .orderByDesc(RewardPoint::getUserId)), RewardPointBackDTO.class);
        int count = rewardPoints.size();
        rewardPoints = rewardPoints.subList((int) ((conditionVO.getCurrent() - 1) * conditionVO.getSize()),
                Math.min((int) (conditionVO.getCurrent() * conditionVO.getSize()), rewardPoints.size()));
        return new PageResult<>(rewardPoints, count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdatePoints(RewardPointVO rewardVO) {
        RewardPoint rewardPoint = BeanCopyUtils.copyObject(rewardVO, RewardPoint.class);
        if (rewardPointDao.selectCount(new LambdaQueryWrapper<RewardPoint>().eq(RewardPoint::getUserId, rewardVO.getUserId())) > 0) {
            rewardPoint.setTotalPoints(rewardPointDao.selectList(new LambdaQueryWrapper<RewardPoint>().eq(RewardPoint::getUserId, rewardVO.getUserId())
                    .orderByDesc(RewardPoint::getId)).get(0).getTotalPoints() + rewardVO.getPoints());
        } else {
            rewardPoint.setTotalPoints(rewardVO.getPoints());
        }
        this.saveOrUpdate(rewardPoint);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateAddress(UserAddressVO userAddressVO) {
        if (Objects.nonNull(userAddressVO.getIsDefault()) && userAddressVO.getIsDefault()) {
            UserAddress address = userAddressDao.selectOne(new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getIsDefault, true)
                    .eq(UserAddress::getIsDelete, 0).eq(UserAddress::getUserInfoId, UserUtils.getLoginUser().getUserInfoId()));
            if (Objects.nonNull(address)) {
                address.setIsDefault(false);
                userAddressDao.updateById(address);
            }
        }
        UserAddress userAddress = BeanCopyUtils.copyObject(userAddressVO, UserAddress.class);
        userAddress.setUserInfoId(UserUtils.getLoginUser().getUserInfoId());
        userAddress.setReceiverName(HTMLUtils.filter(userAddress.getReceiverName()));
        userAddressServiceImpl.saveOrUpdate(userAddress);
    }

    @Override
    public List<UserAddressDTO> listAddress(ConditionVO conditionVO) {
        List<UserAddressDTO> userAddressDTOS = BeanCopyUtils.copyList(userAddressDao.selectList(
                new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getUserInfoId, UserUtils.getLoginUser().getUserInfoId())
                        .eq(UserAddress::getIsDelete, false)
                        .like(Objects.nonNull(conditionVO.getKeywords()), UserAddress::getDetailAddress, conditionVO.getKeywords())
                        .orderByDesc(UserAddress::getIsDefault).orderByDesc(UserAddress::getId)), UserAddressDTO.class);
        for (UserAddressDTO userAddressDTO : userAddressDTOS) {
            userAddressDTO.setRegion(regionDao.selectById(userAddressDTO.getRegionCode()));
        }
        if (userAddressDTOS.size() > 5) {
            return userAddressDTOS.subList(0, 5);
        } else {
            return userAddressDTOS;
        }
    }

    @Override
    public PageResult<UserAddressBackDTO> listAddressBack(ConditionVO conditionVO) {
        List<UserAddressBackDTO> userAddressDTOS = BeanCopyUtils.copyList(userAddressDao.selectList(
                new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getIsDelete, false)
                        .like(Objects.nonNull(conditionVO.getKeywords()), UserAddress::getDetailAddress, conditionVO.getKeywords())
                        .eq(Objects.nonNull(conditionVO.getUserInfoId()), UserAddress::getUserInfoId, conditionVO.getUserInfoId())
                        .orderByDesc(UserAddress::getIsDefault).orderByDesc(UserAddress::getId)), UserAddressBackDTO.class);
        for (UserAddressBackDTO userAddressDTO : userAddressDTOS) {
            userAddressDTO.setRegion(regionDao.selectById(userAddressDTO.getRegionCode()));
            userAddressDTO.setNickname(userInfoDao.selectById(userAddressDTO.getUserInfoId()).getNickname());
        }
        if (Objects.nonNull(conditionVO.getSize()) && Objects.nonNull(conditionVO.getCurrent())) {
            userAddressDTOS = userAddressDTOS.subList((int) ((conditionVO.getCurrent() - 1) * conditionVO.getSize()),
                    Math.min((int) (conditionVO.getCurrent() * conditionVO.getSize()), userAddressDTOS.size()));
        }
        return new PageResult<>(userAddressDTOS, userAddressDTOS.size());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateSPU(SPUVO spuVO) {
        spUServiceImpl.saveOrUpdate(BeanCopyUtils.copyObject(spuVO, SPU.class));
    }

    @Override
    public PageResult<SPUBackDTO> listSPUBacks(ConditionVO condition) {
        // 查询spu总量
        Integer count = spuDao.countSPUBacks(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台spu
        List<SPUBackDTO> spuBackDTOS = spuDao.listSPUBacks(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        // 查询spu浏览量
        Map<Object, Double> viewsCountMap = redisService.zAllScore(RedisPrefixConst.SPU_VIEWS_COUNT);
        // 封装浏览量
        spuBackDTOS.forEach(item -> {
            Double viewsCount = viewsCountMap.get(item.getId());
            if (Objects.nonNull(viewsCount)) {
                item.setViewsCount(viewsCount.intValue());
            }
            // 转换图片格式
            if (Objects.nonNull(item.getPictures())) {
                item.setPictureList(CommonUtils.castList(JSON.parseObject(item.getPictures(), List.class), String.class));
            }
            // 查询销量
            QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
            Map<String, Object> map = orderItemService.getMap(queryWrapper.select("sum(num) as num")
                    .eq("is_delete", 0).ne("status", 4).eq("spu_id", item.getId()));
            if (Objects.nonNull(map)) {
                item.setSalesCount(Integer.parseInt(map.get("num").toString()));
            } else {
                item.setSalesCount(0);
            }
            item.getSkuBackDTOList().forEach(sku -> {
                QueryWrapper<OrderItem> queryWrapper1 = new QueryWrapper<>();
                Map<String, Object> map1 = orderItemService.getMap(queryWrapper1.select("sum(num) as num")
                        .eq("is_delete", 0).ne("status", 4).eq("sku_id", sku.getId()));
                if (Objects.nonNull(map1)) {
                    sku.setSalesCount(Integer.parseInt(map1.get("num").toString()));
                } else {
                    sku.setSalesCount(0);
                }
            });
        });
        return new PageResult<>(spuBackDTOS, count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateSKU(SKUVO skuVO) {
        skuService.saveOrUpdate(BeanCopyUtils.copyObject(skuVO, SKU.class));
    }

    @Override
    public PageResult<SPUHomeDTO> listSPUs(ConditionVO condition) {
        condition.setIsDelete(0);
        condition.setIsOn(1);
        // 查询spu总量
        Integer count = spuDao.countSPUBacks(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台spu
        List<SPUBackDTO> spuBackDTOS = spuDao.listSPUBacks(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        List<SPUHomeDTO> spuHomeDTOS = new ArrayList<>();
        // 获取最低积分
        spuBackDTOS.forEach(item -> {
            SPUHomeDTO spuHomeDTO = BeanCopyUtils.copyObject(item, SPUHomeDTO.class);
            // 转换图片格式
            if (Objects.nonNull(item.getPictures())) {
                spuHomeDTO.setCover(CommonUtils.castList(JSON.parseObject(item.getPictures(), List.class), String.class).get(0));
            }
            spuHomeDTO.setPoints(item.getSkuBackDTOList().stream().map(SKUBackDTO::getPoints).mapToInt(item1 -> item1).
                    filter(item1 -> item1 <= 999).min().orElse(999));
            // 查询销量
            QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
            Map<String, Object> map = orderItemService.getMap(queryWrapper.select("sum(num) as num")
                    .eq("is_delete", 0).ne("status", 4).eq("spu_id", item.getId()));
            if (Objects.nonNull(map)) {
                spuHomeDTO.setSalesCount(Integer.parseInt(map.get("num").toString()));
            } else {
                spuHomeDTO.setSalesCount(0);
            }
            spuHomeDTOS.add(spuHomeDTO);
        });
        //按价格、销量排序
        if (Objects.nonNull(condition.getType())) {
            switch (condition.getType()) {
                case 1:
                    spuHomeDTOS.sort((d1, d2) -> d2.getPoints() - d1.getPoints());
                    break;
                case 2:
                    spuHomeDTOS.sort(Comparator.comparingInt(SPUHomeDTO::getPoints));
                    break;
                case 3:
                    spuHomeDTOS.sort((d1, d2) -> d2.getSalesCount() - d1.getSalesCount());
                    break;
                default:
                    break;
            }
        }
        return new PageResult<>(spuHomeDTOS, count);
    }

    @Override
    public SPUDTO getSPUById(Integer spuId) {
        SPU spu = spuDao.selectById(spuId);
        SPUDTO spudto = BeanCopyUtils.copyObject(spu, SPUDTO.class);
        spudto.setPictureList(CommonUtils.castList(JSON.parseObject(spu.getPictures(), List.class), String.class));
        spudto.setSkuDTOList(BeanCopyUtils.copyList(skuDao.selectList(new LambdaQueryWrapper<SKU>()
                .eq(SKU::getSpuId, spuId).eq(SKU::getIsDelete, false)), SKUDTO.class));
        spudto.setMinPoints(spudto.getSkuDTOList().stream().map(SKUDTO::getPoints).mapToInt(item -> item).
                filter(item -> item <= 999).min().orElse(999));
        // 判断是否第一次访问，增加浏览量
        Set<Integer> spuSet = CommonUtils.castSet(Optional.ofNullable(session.getAttribute(CommonConst.SPU_SET)).orElseGet(HashSet::new), Integer.class);
        if (!spuSet.contains(spuId)) {
            spuSet.add(spuId);
            session.setAttribute(CommonConst.SPU_SET, spuSet);
            // 浏览量+1
            redisService.zIncr(RedisPrefixConst.SPU_VIEWS_COUNT, spuId, 1D);
        }
        // 查询销量
        QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
        Map<String, Object> map = orderItemService.getMap(queryWrapper.select("sum(num) as num")
                .eq("is_delete", 0).ne("status", 4).eq("spu_id", spudto.getId()));
        if (Objects.nonNull(map)) {
            spudto.setSalesCount(Integer.parseInt(map.get("num").toString()));
        } else {
            spudto.setSalesCount(0);
        }
        return spudto;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateShoppingCart(ShoppingCartVO shoppingCartVO) {
        ShoppingCart shoppingCart = BeanCopyUtils.copyObject(shoppingCartVO, ShoppingCart.class);
        shoppingCart.setUserId(UserUtils.getLoginUser().getUserInfoId());
        if (Objects.nonNull(shoppingCartVO.getId())) {
            shoppingCart = shoppingCartDao.selectById(shoppingCartVO.getId());
            if (Objects.nonNull(shoppingCartVO.getNum())) {
                shoppingCart.setNum(shoppingCartVO.getNum());
                if (shoppingCart.getNum() > skuDao.selectById(shoppingCart.getSkuId()).getStock()) {
                    throw new BizException("库存不足");
                }
            }
            shoppingCart.setNum(shoppingCartVO.getNum());
            if (Objects.nonNull(shoppingCart.getIsDelete())) {
                shoppingCart.setIsDelete(shoppingCartVO.getIsDelete());
            }
        } else {
            shoppingCart.setSpuId(skuDao.selectById(shoppingCart.getSkuId()).getSpuId());
            if (Objects.isNull(shoppingCart.getNum())) {
                ShoppingCart shoppingCart1 = shoppingCartDao.selectOne(new LambdaQueryWrapper<ShoppingCart>()
                        .eq(ShoppingCart::getUserId, shoppingCart.getUserId())
                        .eq(ShoppingCart::getSkuId, shoppingCart.getSkuId())
                        .eq(ShoppingCart::getIsDelete, 0));
                if (Objects.nonNull(shoppingCart1)) {
                    shoppingCart.setNum(shoppingCart1.getNum() + shoppingCartVO.getAddNum());
                    shoppingCart.setId(shoppingCart1.getId());
                } else {
                    shoppingCart.setNum(shoppingCartVO.getAddNum());
                    shoppingCart.setAddPoints(skuDao.selectById(shoppingCart.getSkuId()).getPoints());
                }
            }
            if (shoppingCart.getNum() > skuDao.selectById(shoppingCart.getSkuId()).getStock()) {
                throw new BizException("库存不足");
            }
        }

        shoppingCartService.saveOrUpdate(shoppingCart);
    }

    @Override
    public List<ShoppingCartDTO> getShoppingCartByIds(String cartIds) {
        String cartIds1 = "[" + cartIds + "]";
        List<Integer> cartIdList = CommonUtils.castList(JSON.parseObject(cartIds1, List.class), Integer.class);
        List<ShoppingCartDTO> shoppingCartDTOList = new ArrayList<>();
        for (Integer integer : cartIdList) {
            ShoppingCartDTO shoppingCartDTO = BeanCopyUtils.copyObject(shoppingCartDao.selectById(integer), ShoppingCartDTO.class);
            shoppingCartDTO.setSpuName(spuDao.selectById(shoppingCartDTO.getSpuId()).getName());
            shoppingCartDTO.setIsOn(spuDao.selectById(shoppingCartDTO.getSpuId()).getIsOn());
            shoppingCartDTO.setShoppingCartSKUDTO(BeanCopyUtils.copyObject(
                    skuDao.selectById(shoppingCartDao.selectById(integer).getSkuId()), ShoppingCartSKUDTO.class));
            shoppingCartDTO.setSubPoints(shoppingCartDao.selectById(integer).getAddPoints()
                    - shoppingCartDTO.getShoppingCartSKUDTO().getPoints());
            shoppingCartDTOList.add(shoppingCartDTO);
        }
        return shoppingCartDTOList;
    }

    @Override
    public PageResult<ShoppingCartDTO> listShoppingCart(ConditionVO condition) {
        condition.setUserInfoId(UserUtils.getLoginUser().getUserInfoId());
        // 查询购物车总量
        Integer count = shoppingCartDao.countShoppingCart(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询购物车
        List<ShoppingCartDTO> shoppingCartList = shoppingCartDao.listShoppingCart(PageUtils.getLimitCurrent(),
                PageUtils.getSize(), condition);
        return new PageResult<>(shoppingCartList, count);
    }

    @Override
    public PageResult<ShoppingCartBackDTO> listShoppingCartBack(ConditionVO condition) {
        Integer count = shoppingCartDao.countShoppingCart(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询购物车
        List<ShoppingCartBackDTO> shoppingCartList = shoppingCartDao.listShoppingCartBacks(PageUtils.getLimitCurrent(),
                PageUtils.getSize(), condition);
        return new PageResult<>(shoppingCartList, count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateOrder(OrderVO orderVO) {
        //确认收货或改地址
        if (Objects.nonNull((orderVO.getId()))) {
            Order order = BeanCopyUtils.copyObject(orderVO, Order.class);
            orderService.updateById(order);
            return;
        }
        //下单
        Order order = BeanCopyUtils.copyObject(orderVO, Order.class);
        order.setOrderCode(SnowflakeIdUtils.getUUID());
        order.setUserId(UserUtils.getLoginUser().getUserInfoId());
        orderService.saveOrUpdate(order);
        int totalPoints = 0, rewardPoints;
        rewardPoints = rewardPointDao.selectList(new LambdaQueryWrapper<RewardPoint>()
                .eq(RewardPoint::getUserId, UserUtils.getLoginUser().getUserInfoId())
                .orderByDesc(RewardPoint::getCreateTime)).get(0).getTotalPoints();
        List<SKU> skuList = new ArrayList<>();
        for (Integer skuId : orderVO.getOrderItems().keySet()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setSpuId(skuDao.selectById(skuId).getSpuId());
            orderItem.setSkuId(skuId);
            orderItem.setNum(orderVO.getOrderItems().get(skuId));
            orderItem.setPoints(skuDao.selectById(skuId).getPoints());
            totalPoints += orderItem.getPoints() * orderItem.getNum();
            if (totalPoints > rewardPoints) {
                rollBackOrder(order);
                throw new BizException("积分不足，订单取消");
            }
            if (skuDao.selectById(skuId).getStock() < orderItem.getNum()) {
                rollBackOrder(order);
                throw new BizException("库存不足，订单取消");
            }
            orderItemService.save(orderItem);
            skuList.add(SKU.builder().id(skuId).stock(skuDao.selectById(skuId).getStock() - orderItem.getNum()).build());
        }
        skuService.updateBatchById(skuList);
        if (Objects.nonNull(orderVO.getShoppingCartIds())) {
            for (Integer shoppingCartId : orderVO.getShoppingCartIds()) {
                ShoppingCart shoppingCart = new ShoppingCart();
                shoppingCart.setId(shoppingCartId);
                shoppingCart.setIsDelete(1);
                shoppingCartService.updateById(shoppingCart);
            }
        }
        order.setTotalPoints(totalPoints);
        orderService.updateById(order);
        RewardPoint rewardPoint = RewardPoint.builder().userId(UserUtils.getLoginUser().getUserInfoId()).event("购买商品")
                .points(-totalPoints).totalPoints(rewardPoints - totalPoints).build();
        this.save(rewardPoint);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOrder(OrderBackVO orderBackVO) {
        Order order = BeanCopyUtils.copyObject(orderBackVO, Order.class);
        List<Integer> freightIds = new ArrayList<>();
        //发货
        if (Objects.nonNull(orderBackVO.getFreights()) && orderBackVO.getStatus() == 2) {
            for (Map<String, Object> freightMap : orderBackVO.getFreights()) {
                if (Objects.isNull(freightDao.selectOne(new LambdaQueryWrapper<Freight>()
                        .eq(Freight::getFreightCode, freightMap.get("freightCode"))
                        .eq(Freight::getFreightBrand, freightMap.get("freightBrand"))
                        .eq(Freight::getIsDelete, 0)))) {
                    Freight freight = Freight.builder().freightCode((String) freightMap.get("freightCode"))
                            .freightBrand((String) freightMap.get("freightBrand"))
                            .price((Double) freightMap.get("price")).build();
                    freightDao.insert(freight);
                    freightIds.add(freight.getId());
                } else {
                    Freight freight = Freight.builder().id(freightDao.selectOne(new LambdaQueryWrapper<Freight>()
                            .eq(Freight::getFreightCode, freightMap.get("freightCode"))
                            .eq(Freight::getFreightBrand, freightMap.get("freightBrand"))
                            .eq(Freight::getIsDelete, 0)).getId()).build();
                    freightIds.add(freight.getId());
                    for (String s : freightMap.keySet()) {
                        switch (s) {
                            case "isDelete":
                                freight.setIsDelete((Integer) freightMap.get("isDelete"));
                                if ((Integer) freightMap.get("isDelete") == 1) {
                                    freightIds.remove(freightIds.size() - 1);
                                }
                                break;
                            case "price":
                                freight.setPrice((Double) freightMap.get("price"));
                                break;
                            default:
                                break;
                        }
                    }
                    freightDao.updateById(freight);
                }
            }
        }
        order.setFreightIds(JSON.toJSONString(freightIds));
        order.setEndTime(LocalDateTime.now().plusDays(8));
        orderService.updateById(order);
    }

    @Override
    public PageResult<OrderDTO> listOrders(ConditionVO condition) {
        condition.setUserInfoId(UserUtils.getLoginUser().getUserInfoId());
        condition.setIsDelete(0);
        return listOrdersBacks(condition);
    }

    @Override
    public PageResult<OrderDTO> listOrdersBacks(ConditionVO condition) {
        // 查询订单总量
        Integer count = orderDao.countOrders(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询订单
        List<OrderDTO> orderList = orderDao.listOrders(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        for (OrderDTO orderDTO : orderList) {
            List<Integer> freightIdList = CommonUtils.castList(JSON.parseObject(orderDTO.getFreightIds(), List.class), Integer.class);
            if (freightIdList.size() > 0) {
                orderDTO.setFreights(BeanCopyUtils.copyList(freightDao.selectBatchIds(freightIdList), FreightDTO.class));
            }
        }
        return new PageResult<>(orderList, count);
    }

    @Override
    public PageResult<FreightBackDTO> listFreightBacks(ConditionVO conditionVO) {
        int count = Math.toIntExact(freightDao.selectCount(new LambdaQueryWrapper<Freight>()
                .eq(Objects.nonNull(conditionVO.getFreightId()), Freight::getId, conditionVO.getFreightId())
                .eq(Objects.nonNull(conditionVO.getIsDelete()), Freight::getIsDelete, conditionVO.getIsDelete())
                .like(Objects.nonNull(conditionVO.getKeywords()), Freight::getFreightBrand, conditionVO.getKeywords())));
        if (count == 0) {
            return new PageResult<>();
        }
        List<FreightBackDTO> freightBackDTOS = BeanCopyUtils.copyList(freightDao.selectList(new LambdaQueryWrapper<Freight>()
                        .eq(Objects.nonNull(conditionVO.getFreightId()), Freight::getId, conditionVO.getFreightId())
                        .eq(Objects.nonNull(conditionVO.getIsDelete()), Freight::getIsDelete, conditionVO.getIsDelete())
                        .like(Objects.nonNull(conditionVO.getKeywords()), Freight::getFreightBrand, conditionVO.getKeywords())),
                FreightBackDTO.class);
        if (Objects.nonNull(conditionVO.getCurrent())) {
            freightBackDTOS = freightBackDTOS.subList((int) ((conditionVO.getCurrent() - 1) * conditionVO.getSize()),
                    Math.min((int) (conditionVO.getCurrent() * conditionVO.getSize()), freightBackDTOS.size()));
        }
        return new PageResult<>(freightBackDTOS, count);
    }

    @Override
    public void cancelOrder(Integer orderId) {
        Order order = orderDao.selectById(orderId);
        if (orderDao.selectById(orderId).getStatus() != 1) {
            throw new BizException("订单已发货，不能取消");
        }
        order.setStatus(4);
        orderDao.updateById(order);
        RewardPoint rewardPoint = RewardPoint.builder().points(order.getTotalPoints())
                .userId(UserUtils.getLoginUser().getUserInfoId())
                .totalPoints(listRewardPoints().get(0).getTotalPoints() + order.getTotalPoints()).event("积分退回").build();
        this.save(rewardPoint);
    }


    private void rollBackOrder(Order order) {
        order.setStatus(4);
        order.setIsDelete(1);
        orderService.updateById(order);
        OrderItem item = OrderItem.builder().status(4).isDelete(1).build();
        orderItemDao.update(item, new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId()));
    }


}
