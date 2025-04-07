package com.sharefile.securedoc.cache;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean(name = {"userloginCache"})
//even if we have multiple cache store bean we can inject specific bean base on the name
    public CacheStore<String, Integer> userCacheStore() {
        return new CacheStore<String, Integer>(900, TimeUnit.SECONDS);
    }
/*
    @Bean(name = {"registrationCache"})
    public CacheStore<String, String> regestrationCacheStore() {
        return new CacheStore<String, String>(600, TimeUnit.MINUTES);
    }
*/
}