package com.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xinyuan Xu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoDTO {
    /**
     * 任务 id
     */
    private String taskId;

    /**
     * 推流主进程
     */
    private Process process;

    /**
     * 标准流监听器
     */
    private StandardStreamListenerDTO standardListener;

    /**
     * 异常流监听器
     */
    private ErrorStreamListenerDTO errorListener;
}
