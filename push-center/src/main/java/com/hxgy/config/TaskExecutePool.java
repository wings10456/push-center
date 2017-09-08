/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 * 
 * @author WindsYan
 * @version $Id: TaskExecutePool.java, v 0.1 2017年8月7日 下午3:04:27 WindsYan Exp $
 */
@Configuration  
@EnableAsync
public class TaskExecutePool {

    @Value("${taskExecutePoolConf.threadPoolSize}")
    private int threadPoolSize;
    @Value("${taskExecutePoolConf.threadNamePrefix}")
    private String threadNamePrefix;
    @Value("${taskExecutePoolConf.maxPoolSize}")
    private int maxPoolSize;
    @Value("${taskExecutePoolConf.queueCapacity}")
    private int queueCapacity;
    @Value("${taskExecutePoolConf.keepAliveSeconds}")
    private int keepAliveSeconds;
	
    /**
     * USER线程池
     * @return
     */
    @Bean  
    public Executor userAsyncPool() {
        return genExecutor(threadPoolSize,maxPoolSize,
        		queueCapacity,keepAliveSeconds,
        		threadNamePrefix);
    }
    
    private Executor genExecutor(int threadPoolSize, int maxPoolSize, int queueCapacity, int keepAliveSeconds, String threadNamePrefix) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix(threadNamePrefix);
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
