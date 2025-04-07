package com.sharefile.securedoc.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class CacheStore<K, V> {
    private Cache<K, V> cache;

    public CacheStore(int expireDuration, TimeUnit timeUnit) {
        cache = CacheBuilder.newBuilder()
            .expireAfterWrite(expireDuration, timeUnit)
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .build();
    }

    public V get(@NonNull K key) {
        log.info("Getting cached value from cache from key {} ", key);
        return cache.getIfPresent(key);
    }

    public void put(@NonNull K key, @NonNull V value) {
        log.info("Storing record in cache for key {} ", key);
        cache.put(key, value);
    }

    public void evict(@NonNull K key) {
        log.info("Removing record in cache for key {} ", key);
        cache.invalidate(key);
    }
}