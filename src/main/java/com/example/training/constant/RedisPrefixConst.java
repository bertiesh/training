package com.example.training.constant;

/**
 * @author Xinyuan Xu
 */
public class RedisPrefixConst {
    /**
     * code expire time
     */
    public static final long CODE_EXPIRE_TIME = 15 * 60;

    /**
     * code
     */
    public static final String USER_CODE_KEY = "code:";

    /**
     * captcha expire time
     */
    public static final long CAPTCHA_EXPIRE_TIME = 60;

    /**
     * captcha
     */
    public static final String USER_CAPTCHA_KEY = "captcha:";

    /**
     * blog view
     */
    public static final String BLOG_VIEWS_COUNT = "blog_views_count";

    /**
     * article view
     */
    public static final String ARTICLE_VIEWS_COUNT = "article_views_count";

    /**
     * collection view
     */
    public static final String COLLECTION_VIEWS_COUNT = "collection_views_count";

    /**
     * article like
     */
    public static final String ARTICLE_LIKE_COUNT = "article_like_count";

    /**
     * user article like
     */
    public static final String ARTICLE_USER_LIKE = "article_user_like:";

    /**
     * talk article like
     */
    public static final String TALK_LIKE_COUNT = "talk_like_count";

    /**
     * user talk like
     */
    public static final String TALK_USER_LIKE = "talk_user_like:";

    /**
     * comment like
     */
    public static final String COMMENT_LIKE_COUNT = "comment_like_count";

    /**
     * user comment like
     */
    public static final String COMMENT_USER_LIKE = "comment_user_like:";

    /**
     * view
     */
    public static final String DOCUMENT_HISTORY_COUNT = "document_history_count";

    /**
     * user view
     */
    public static final String DOCUMENT_USER_HISTORY = "document_user_history:";

    /**
     * view
     */
    public static final String MOVIE_HISTORY_COUNT = "movie_history_count";

    /**
     * user view
     */
    public static final String MOVIE_USER_HISTORY = "movie_user_history:";

    /**
     * spu view
     */
    public static final String SPU_VIEWS_COUNT = "spu_views_count";

    /**
     * web config
     */
    public static final String WEBSITE_CONFIG = "website_config";

    /**
     * user area
     */
    public static final String USER_AREA = "user_area";

    /**
     * visitor area
     */
    public static final String VISITOR_AREA = "visitor_area";

    /**
     * page cover
     */
    public static final String PAGE_COVER = "page_cover";

    /**
     * about me
     */
    public static final String ABOUT = "about";

    /**
     * visitor
     */
    public static final String UNIQUE_VISITOR = "unique_visitor";
}
