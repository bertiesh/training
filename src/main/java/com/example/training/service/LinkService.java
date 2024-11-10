package com.example.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.training.dto.LinkBackDTO;
import com.example.training.dto.LinkDTO;
import com.example.training.entity.Link;
import com.example.training.vo.ConditionVO;
import com.example.training.vo.LinkVO;
import com.example.training.vo.PageResult;

import java.util.List;

/**
 * @author Xuxinyuan
 */
public interface LinkService extends IService<Link> {
    /**
     * 查看友链列表
     *
     * @return 友链列表
     */
    List<LinkDTO> listFriendLinks();

    /**
     * 查看后台友链列表
     *
     * @param condition 条件
     * @return 友链列表
     */
    PageResult<LinkBackDTO> listFriendLinkDTO(ConditionVO condition);

    /**
     * 保存或更新友链
     *
     * @param friendLinkVO 友链
     */
    void saveOrUpdateFriendLink(LinkVO friendLinkVO);
}
