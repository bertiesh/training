package com.example.training.controller;

import com.example.training.annotation.OptLog;
import com.example.training.dto.LinkBackDTO;
import com.example.training.dto.LinkDTO;
import com.example.training.service.LinkService;
import com.example.training.vo.ConditionVO;
import com.example.training.vo.LinkVO;
import com.example.training.vo.PageResult;
import com.example.training.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.training.constant.OptTypeConst.REMOVE;
import static com.example.training.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * @author Xinyuan Xu
 */
@Api(tags = "链接模块")
@RestController
public class LinkController {
    @Autowired
    private LinkService linkService;

    /**
     * search link list
     *
     * @return {@link Result <FriendLinkDTO>} link list
     */
    @ApiOperation(value = "查看链接列表")
    @GetMapping("/links")
    public Result<List<LinkDTO>> listFriendLinks() {
        return Result.ok(linkService.listFriendLinks());
    }

    /**
     * search backend link list
     *
     * @param condition condition
     * @return {@link Result< LinkBackDTO >} backend link list
     */
    @ApiOperation(value = "查看后台链接列表")
    @GetMapping("/admin/links")
    public Result<PageResult<LinkBackDTO>> listFriendLinkDTO(ConditionVO condition) {
        return Result.ok(linkService.listFriendLinkDTO(condition));
    }

    /**
     * save/update link list
     *
     * @param friendLinkVO link info
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或修改链接")
    @PostMapping("/admin/links")
    public Result<?> saveOrUpdateFriendLink(@Valid @RequestBody LinkVO friendLinkVO) {
        linkService.saveOrUpdateFriendLink(friendLinkVO);
        return Result.ok();
    }

    /**
     * delete link
     *
     * @param linkIdList link id list
     * @return {@link Result<>}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除链接")
    @DeleteMapping("/admin/links")
    public Result<?> deleteFriendLink(@RequestBody List<Integer> linkIdList) {
        linkService.removeByIds(linkIdList);
        return Result.ok();
    }
}
