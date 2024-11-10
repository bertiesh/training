package com.example.training.util;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import com.example.training.dto.ErrorStreamListenerDTO;
import com.example.training.dto.StandardStreamListenerDTO;
import com.example.training.dto.VideoDTO;
import com.example.training.exception.BizException;
import com.example.training.vo.VideoVO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.example.training.constant.CommonConst.*;

/**
 * @author Xuxinyuan
 */
public class VideoStreamUtils {
    private static final ConcurrentHashMap<String, VideoDTO> STREAM_MAP = new ConcurrentHashMap();

    public static synchronized String play(VideoVO videoVO) {
        VideoDTO video = STREAM_MAP.get(videoVO.getVideoURI());
        if (Objects.isNull(video)) {
            String taskId = IdUtil.fastSimpleUUID();
            Process process = RuntimeUtil.exec(buildCommand(videoVO.getVideoURI(), taskId));
            CountDownLatch countDownLatch = new CountDownLatch(ONE_THREAD);
            ErrorStreamListenerDTO errorListener = ErrorStreamListenerDTO.builder().videoURI(videoVO.getVideoURI())
                    .reader(new BufferedReader(new InputStreamReader(process.getErrorStream()))).build();
            StandardStreamListenerDTO standardListener = StandardStreamListenerDTO.builder().videoURI(videoVO.getVideoURI())
                    .reader(new BufferedReader(new InputStreamReader(process.getInputStream()))).countDownLatch(countDownLatch)
                    .hlsFilePath(StrBuilder.create(PathUtils.getApplicationJarHome()).append(taskId).append(HLS_BASE_URI).toString()).build();
            try {
                ThreadPoolUtil.execute(standardListener);
                ThreadPoolUtil.execute(errorListener);
                boolean await = countDownLatch.await(videoVO.getTimeOut(), TimeUnit.SECONDS);
                if (!await){
                    destroy(process, errorListener, standardListener);
                    throw new BizException("视频流推送失败");
                }
                if (!standardListener.getExecuteResult()) {
                    destroy(process, errorListener, standardListener);
                    throw new BizException("视频流推送失败");
                }
                video = VideoDTO.builder().taskId(taskId).process(process).errorListener(errorListener)
                        .standardListener(standardListener).build();
            } catch (Exception e) {
                destroy(process, errorListener, standardListener);
                throw new BizException("视频流推送失败");
            }
            STREAM_MAP.put(videoVO.getVideoURI(), video);
        }
        return StrUtil.format(HLS_BASE_URI, video.getTaskId());
    }

    public static void destroy(String videoURI) {
        VideoDTO taskInfo = STREAM_MAP.get(videoURI);
        if (Objects.nonNull(taskInfo)) {
            destroy(taskInfo.getProcess(), taskInfo.getErrorListener(), taskInfo.getStandardListener());
        }
    }

    private static void destroy(Process process, Runnable... runnables) {
        RuntimeUtil.destroy(process);
        for (Runnable runnable : runnables) {
            if (Objects.nonNull(runnable)) {
                ThreadPoolUtil.remove(runnable);
            }
        }
    }

    private static String buildCommand(String videoURI, String taskId) {
        String baseCommand;
        if (videoURI.startsWith(RTSP_PATTERN)) {
            baseCommand = FFMPEG_PUSH_RTSP_CMD;
        } else {
            baseCommand = FFMPEG_PUSH_RTMP_CMD;
        }
        return StrUtil.format(baseCommand, videoURI, taskId);
    }
}
