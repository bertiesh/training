package com.example.training.service;

import com.example.training.dto.BackInfoDTO;
import com.example.training.dto.HomeInfoDTO;
import com.example.training.dto.WebsiteDTO;
import com.example.training.vo.InfoVO;
import com.example.training.vo.WebsiteConfigVO;

/**
 * @author Xuxinyuan
 */
public interface InfoService {
    /**
     * 获取首页数据
     *
     * @return 博客首页信息
     */
    HomeInfoDTO getBlogHomeInfo(Integer userInfoId);

    /**
     * 获取后台首页数据
     *
     * @param start 起始日期
     * @param end 终止日期
     * @return 后台信息
     */
    BackInfoDTO getBlogBackInfo(String start, String end);

    /**
     * 保存或更新网站配置
     *
     * @param websiteConfigVO 网站配置
     */
    void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO);

    /**
     * 获取网站配置
     *
     * @return {@link WebsiteConfigVO} 网站配置
     */
    WebsiteConfigVO getWebsiteConfig();

    /**
     * 获取关于我内容
     *
     * @return 关于我内容
     */
    String getAbout();

    /**
     * 修改关于我内容
     *
     * @param blogInfoVO 博客信息
     */
    void updateAbout(InfoVO blogInfoVO);

    /**
     * 上传访客信息
     *
     * @return
     */
    WebsiteDTO report();
}
