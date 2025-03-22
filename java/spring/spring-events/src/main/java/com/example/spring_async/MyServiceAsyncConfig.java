package com.example.spring_async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * When using the @Async annotation by default, in actually, your app uses SimpleAsyncTaskExecutor thread pool.
 * In actually this is not true thread pool.When use SimpleAsyncTaskExecutor thread pool, your app can not thread reuse.
 * Your system will be created new one, when it is called.
 * After a while, your system continuously created thread and due to increasing memory using,
 * finally cause an Out of memory Error.
 */

@Configuration
@EnableAsync
public class MyServiceAsyncConfig implements AsyncConfigurer {

    @Bean(name = "myTaskExecutor")
    //-- name is important because in @Async("myTaskExecutor") we specify in which bean it need to run
    public TaskExecutor myTaskExecutor() {
        var threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("Async-");
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(30);
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setQueueCapacity(100);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new MyTaskExecutionHandlerImpl());
        threadPoolTaskExecutor.initialize();

        return threadPoolTaskExecutor;

        //When you use spring security wrap the executor in the DelegatingSecurityContextAsyncTaskExecutor
        // so u can access the security context inside an async method.
        //return new DelegatingSecurityContextAsyncTaskExecutor(threadPoolTaskExecutor);
    }
}

class MyTaskExecutionHandlerImpl implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
            executor.getQueue().put(r);
        } catch (InterruptedException e) {
            throw new RejectedExecutionException(e.getMessage(), e);
        }
    }
}