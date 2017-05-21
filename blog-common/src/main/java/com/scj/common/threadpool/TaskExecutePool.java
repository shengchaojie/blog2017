package com.scj.common.threadpool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@Configuration
@EnableAsync
public class TaskExecutePool {

    @Resource
    private TaskThreadPoolConfig taskThreadPoolConfig;

    @Bean
    public Executor myTaskAsyncPool(){
        ThreadPoolTaskExecutor executor =new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(taskThreadPoolConfig.getCorePoolSize());
        executor.setMaxPoolSize(taskThreadPoolConfig.getMaxPoolSize());
        executor.setKeepAliveSeconds(taskThreadPoolConfig.getMaxPoolSize());
        executor.setQueueCapacity(taskThreadPoolConfig.getQueueCapacity());
        executor.setThreadNamePrefix("MyExecutor-");

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
