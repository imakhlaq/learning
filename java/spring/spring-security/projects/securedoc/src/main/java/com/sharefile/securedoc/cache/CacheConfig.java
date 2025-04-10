package com.sharefile.securedoc.cache;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/*
We're suing this class to do any kind of configuration that we want to do, or any type of key value
pair.
 */

@Configuration
public class CacheConfig {
    @Bean(name = "userLoginCache")
    public CacheStore<String, Integer> userCache() {
        return new CacheStore<>(900, TimeUnit.SECONDS);
    }
//even if we have multiple cache store bean we can inject specific bean base on the name
/*    @Bean(name = "registrationLoginCache")
    public CacheStore<Long, String> anotherCache() {
        return new CacheStore<>(900, TimeUnit.SECONDS);
    }*/
}