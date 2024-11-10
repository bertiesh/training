package com.example.training.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.dao.LinkDao;
import com.example.training.dto.LinkBackDTO;
import com.example.training.dto.LinkDTO;
import com.example.training.entity.Link;
import com.example.training.service.LinkService;
import com.example.training.util.BeanCopyUtils;
import com.example.training.util.PageUtils;
import com.example.training.vo.ConditionVO;
import com.example.training.vo.LinkVO;
import com.example.training.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Xuxinyuan
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkDao, Link> implements LinkService {
    @Autowired
    private LinkDao friendLinkDao;

    @Override
    public List<LinkDTO> listFriendLinks() {
        // 查询友链列表
        List<Link> friendLinkList = friendLinkDao.selectList(null);
        return BeanCopyUtils.copyList(friendLinkList, LinkDTO.class);
    }

    @Override
    public PageResult<LinkBackDTO> listFriendLinkDTO(ConditionVO condition) {
        // 分页查询链接列表
        Page<Link> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        Page<Link> friendLinkPage = friendLinkDao.selectPage(page, new LambdaQueryWrapper<Link>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), Link::getLinkName, condition.getKeywords()));
        // 转换DTO
        List<LinkBackDTO> friendLinkBackDTOList = BeanCopyUtils.copyList(friendLinkPage.getRecords(), LinkBackDTO.class);
        return new PageResult<>(friendLinkBackDTOList, (int) friendLinkPage.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateFriendLink(LinkVO friendLinkVO) {
        Link friendLink = BeanCopyUtils.copyObject(friendLinkVO, Link.class);
        this.saveOrUpdate(friendLink);
    }
}
