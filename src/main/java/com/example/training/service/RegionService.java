package com.example.training.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.training.entity.Region;
import com.example.training.vo.ConditionVO;
import com.example.training.vo.RegionVO;
import com.example.training.vo.Result;

import java.util.List;

/**
 * @author Xuxinyuan
 */
public interface RegionService extends IService<Region> {
    /**
     * 添加或修改行政区划
     *
     * @param regionVO 行政区划
     */
    void saveOrUpdateRegions(RegionVO regionVO);

    /**
     * 删除行政区划
     *
     * @param regionIdList 行政区划id列表
     */
    void deleteRegions(List<Integer> regionIdList);

    /**
     * 查询行政区划列表
     *
     * @param conditionVO 条件
     * @return {@link Result <Tree>} 行政区划列表
     */
    List<Tree<String>> listRegions(ConditionVO conditionVO);
}
