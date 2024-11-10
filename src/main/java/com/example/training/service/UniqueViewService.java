package com.example.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.training.dto.UniqueViewDTO;
import com.example.training.entity.UniqueView;

import java.util.List;

/**
 * 用户量统计
 * @author Xuxinyuan
 */
public interface UniqueViewService extends IService<UniqueView> {
    /**
     * 获取7天用户量统计
     *
     * @return 用户量
     */
    List<UniqueViewDTO> listUniqueViews();
}
