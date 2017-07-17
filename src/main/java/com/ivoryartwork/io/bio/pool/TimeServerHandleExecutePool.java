package com.ivoryartwork.io.bio.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yaochao
 * @version 1.0
 * @date 2017/7/17
 */
public class TimeServerHandleExecutePool {

    private ExecutorService service;

    public TimeServerHandleExecutePool(int maxPoolSize, int maxQueueSize) {
        service = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxPoolSize, 120L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(maxQueueSize));
    }

    public void execute(Runnable task) {
        service.execute(task);
    }
}
