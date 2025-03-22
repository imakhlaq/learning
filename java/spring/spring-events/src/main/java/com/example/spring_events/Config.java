package com.example.spring_events;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * TODO When use SimpleAsyncTaskExecutor thread pool, your app can not thread reuse.
 * <p>
 * By default, the listeners are processed synchronously to make them work Async.
 * We have to create a bean of ApplicationEventMulticaster and set executor to SimpleAsyncTaskExecutor
 * <p>
 * OR
 * <p>
 * You can make listeners async by
 * - @EnableAsync ( top of @SpringBootApp )
 * - @Async ( on top of event listener )
 */

@Configuration
public class Config {

    /*@Bean
    public ApplicationEventMulticaster applicationEventMulticaster() {

        var appEventMulticaster = new SimpleApplicationEventMulticaster();
        appEventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return appEventMulticaster;
    }*/
}