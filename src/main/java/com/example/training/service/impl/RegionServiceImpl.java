package com.example.training.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.dao.RegionDao;
import com.example.training.entity.Region;
import com.example.training.service.RegionService;
import com.example.training.util.BeanCopyUtils;
import com.example.training.vo.ConditionVO;
import com.example.training.vo.RegionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Xuxinyuan
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionDao, Region> implements RegionService {
    @Autowired
    private RegionDao regionDao;

    @Override
    public void saveOrUpdateRegions(RegionVO regionVO) {
        // 保存或修改行政区划
        Region region = BeanCopyUtils.copyObject(regionVO, Region.class);
        this.saveOrUpdate(region);
    }

    @Override
    public void deleteRegions(List<Integer> regionIdList) {
        // 删除行政区划
        regionDao.deleteBatchIds(regionIdList);
    }

    @Override
    public List<Tree<String>> listRegions(ConditionVO conditionVO) {
        //获取所有行政区划信息
        List<Region> regions = regionDao.selectList(new LambdaQueryWrapper<Region>()
                .eq(Objects.nonNull(conditionVO.getType()), Region::getCode, conditionVO.getType()));
        //转换器
        return regionToTree(regions, "0");
    }

    private List<Tree<String>> regionToTree(List<Region> regions, String id) {
        // 构建node列表
        List<TreeNode<String>> nodeList = CollUtil.newArrayList();
        regions.forEach(e -> {
            TreeNode<String> treeNode = new TreeNode<>(e.getCode(), e.getParentCode(), e.getName(), e.getSort());
            Map<String, Object> extra = CollUtil.newHashMap();
            extra.put("code", e.getCode());
            extra.put("province", e.getProvinceName());
            extra.put("city", e.getCityName());
            extra.put("district", e.getDistrictName());
            extra.put("town", e.getTownName());
            extra.put("village", e.getVillageName());
            extra.put("remark", e.getRemark());
            extra.put("sort", e.getSort());
            extra.put("weight", e.getLevel());
            treeNode.setExtra(extra);
            nodeList.add(treeNode);
        });

        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setParentIdKey("parentId");
        //转换器
        return TreeUtil.build(nodeList, id, treeNodeConfig, (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getParentId());
                    tree.setWeight(treeNode.getWeight());
                    tree.setName(treeNode.getName());
                    tree.putAll(treeNode.getExtra());
                });
    }
}
