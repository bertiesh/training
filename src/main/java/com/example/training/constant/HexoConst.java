package com.example.training.constant;

/**
 * Hexo article const
 *  <p>
 *  Hexo construction
 *  ---
 *  title: xxx
 *  date: yyyy-MM-dd HH:mm:ss
 *  categories: [a, b, c]
 *  tags: [d, e, f]
 *  ---
 *  paragraph
 *
 * @author Xinyuan Xu
 */
public class HexoConst {
    /**
     * title
     */
    public static final String TITLE_PREFIX = "title:";

    /**
     * date
     */
    public static final String DATE_PREFIX = "date:";

    /**
     * category
     */
    public static final String CATEGORIES_PREFIX = "categories:";

    /**
     * tag
     */
    public static final String TAGS_PREFIX = "tags:";

    /**
     * delimiter
     */
    public static final String DELIMITER = "---";

    /**
     * prefix
     */
    public static final String PREFIX = "-";

    /**
     * normal flag
     */
    public static final Integer NORMAL_FLAG = 0;

    /**
     * category flag
     */
    public static final Integer CATEGORY_FLAG = 1;

    /**
     * tag flag
     */
    public static final Integer TAG_FLAG = 2;

    /**
     * new line
     */
    public static final String NEW_LINE = "\n";
}
