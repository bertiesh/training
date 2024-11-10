package com.example.training.util;

import java.util.concurrent.*;

import static com.example.training.constant.CommonConst.*;

/**
 * @Author: Deng Yunhu
 * @Date: 2019/11/26 15:34
 */
public class ThreadPoolUtil {
    private static final BlockingQueue<Runnable> BLOCKING_QUEUE = new LinkedBlockingQueue<>();
    private static final ThreadPoolExecutor POOL;

    static {
        POOL = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_LIVE_TIME, TimeUnit.MILLISECONDS, BLOCKING_QUEUE,
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    public static void execute(Runnable runnable) {
        POOL.execute(runnable);
    }

    public static void shutDown() {
        boolean shutdown = POOL.isShutdown();
        if (!shutdown) {
            POOL.shutdown();
        }
    }

    public static void shutDownNow() {
        POOL.shutdownNow();
    }

    public static void remove(Runnable runnable) {
        BLOCKING_QUEUE.remove(runnable);
    }
}
