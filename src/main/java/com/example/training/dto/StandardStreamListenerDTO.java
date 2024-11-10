package com.example.training.dto;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.example.training.constant.CommonConst;
import com.example.training.util.VideoStreamUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StandardStreamListenerDTO implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(StandardStreamListenerDTO.class);
    private String videoURI;

    private String hlsFilePath;

    private Boolean executeResult;

    private CountDownLatch countDownLatch;

    private BufferedReader reader;

    @Override
    public void run() {
        try {
            String msg = null;
            while ((msg = reader.readLine()) != null) {
                if (!executeResult) {
                    logger.info("推流信息-->" + msg);
                }
                if (!executeResult && Pattern.matches(CommonConst.PUSH_SUCCESS_REGEX, msg) && FileUtil.exist(hlsFilePath)) {
                    this.executeResult = true;
                    if (Objects.nonNull(countDownLatch)) {
                        countDownLatch.countDown();
                    }
                }
            }
        } catch (IOException e) {
            logger.error("推流IO异常中断");
        } finally {
            IoUtil.close(reader);
            if (Objects.nonNull(countDownLatch)) {
                countDownLatch.countDown();
            }
            VideoStreamUtils.destroy(videoURI);
        }


    }
}
