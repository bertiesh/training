package com.example.training.constant;

/**
 * @author Xinyuan Xu
 */
public class CommonConst {
    /**
     * false
     */
    public static final int FALSE = 0;

    /**
     * true
     */
    public static final int TRUE = 1;

    /**
     * highlight tag
     */
    public static final String PRE_TAG = "<span style='color:#f47466'>";

    /**
     * highlight tag
     */
    public static final String POST_TAG = "</span>";

    /**
     * current page
     */
    public static final String CURRENT = "current";

    /**
     * page number
     */
    public static final String SIZE = "size";

    /**
     * blogger id
     */
    public static final Integer BLOGGER_ID = 1;

    /**
     * default page num
     */
    public static final String DEFAULT_SIZE = "10";

    /**
     * default nickname
     */
    public static final String DEFAULT_NICKNAME = "USER";

    /**
     * read articles set
     */
    public static String ARTICLE_SET = "articleSet";

    /**
     * read collection set
     */
    public static String COLLECTION_SET = "collectionSet";

    /**
     * read spu set
     */
    public static String SPU_SET = "spuSet";

    /**
     * front end layout
     */
    public static String COMPONENT = "Layout";

    /**
     * province
     */
    public static final String PROVINCE = "省";

    /**
     * city
     */
    public static final String CITY = "市";

    /**
     * unknown
     */
    public static final String UNKNOWN = "未知";

    /**
     * JSON format
     */
    public static final String APPLICATION_JSON = "application/json;charset=utf-8";

    /**
     * default configuration id
     */
    public static final Integer DEFAULT_CONFIG_ID = 1;

    /**
     * default password
     */
    public static final String DEFAULT_PASSWORD = "123456";

    public static final String PUSH_SUCCESS_REGEX = ".*(frame|fps|Lsize|speed).*";

    /**
     * ffmpeg push rtsp stream to HLS (Http Live Streaming)
     */
    public static final String FFMPEG_PUSH_RTSP_CMD = "ffmpeg -rtsp_transport tcp -i {} -fflags flush_packets" +
            " -max_delay 10 -flags -global_header -hls_time 10 -hls_list_size 10 -hls_flags 10 -vcodec copy -y {}";

    /**
     * ffmpeg push rtmp stream to HLS (Http Live Streaming)
     */
    public static final String FFMPEG_PUSH_RTMP_CMD = "ffmpeg -re -i {} -fflags flush_packets -max_delay 10 -flags" +
            " -global_header -hls_time 10 -hls_list_size 10 -hls_flags 10 -vcodec copy -y {}";

    /**
     * HLS (Http Live Streaming) Stream access path
     */
    public static final String HLS_BASE_URI = "/live/{}.m3u8";

    public static final String RTSP_PATTERN = "rtsp";

    public static final int MIN_TIME_OUT = 15;

    public static final int ONE_THREAD = 1;

    public static final int CORE_POOL_SIZE = 25;

    public static final int MAX_POOL_SIZE = 35;

    public static final long KEEP_LIVE_TIME = 60L;
}
