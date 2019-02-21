package com.zzqangel.ddd;

import com.zzqangel.ddd.api.EntityCache;
import com.zzqangel.ddd.config.EntityCacheConfiguration;
import com.zzqangel.ddd.model.Entity;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public class DefaultEntityCache implements EntityCache {

    private int cacheSize;

    private final ConcurrentHashMap<Class, ConcurrentLinkedQueue> cache;

    public DefaultEntityCache(EntityCacheConfiguration entityCacheConfiguration) {
        this.cacheSize = entityCacheConfiguration.getCacheSize();
        cache = new ConcurrentHashMap<>();
    }

    public <T extends Entity> T poll(Class<T> tClass) {
        ConcurrentLinkedQueue<T> concurrentLinkedQueue =cache.computeIfAbsent(tClass, tClass1 -> {
           return new ConcurrentLinkedQueue();
        });

//        return null;
    }
}
