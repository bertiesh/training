package com.example.training.dto;

import com.example.training.entity.Article;
import com.example.training.entity.UserInfo;
import com.example.training.vo.PageVO;
import com.example.training.vo.WebsiteConfigVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Xinyuan Xu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeInfoDTO {
    /**
     * articleCount
     */
    private Integer articleCount;

    /**
     * user info
     */
    private UserInfoDTO userInfoDTO;

    /**
     * likedArticleIdList
     */
    private List<Integer> likedArticleIdList;

    /**
     * my articles
     */
    private List<ArchiveDTO> myArchiveDTOList;

    /**
     * my talks
     */
    private List<Integer> myTalkIdList;
}
