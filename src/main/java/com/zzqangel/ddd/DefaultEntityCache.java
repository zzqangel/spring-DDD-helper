package com.zzqangel.ddd;

import com.zzqangel.ddd.api.CacheEntityCreator;
import com.zzqangel.ddd.api.EntityCache;
import com.zzqangel.ddd.config.EntityCacheConfiguration;
import com.zzqangel.ddd.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public class DefaultEntityCache implements EntityCache {

    private int cacheSize;

    private final ConcurrentHashMap<Class, ConcurrentLinkedQueue> cache;
    @Autowired
    private CacheEntityCreator cacheEntityCreator;

    public DefaultEntityCache(EntityCacheConfiguration entityCacheConfiguration) {
        this.cacheSize = entityCacheConfiguration.getCacheSize();
        cache = new ConcurrentHashMap<>();
    }

    public <T extends Entity> T poll(Class<T> tClass) {
        ConcurrentLinkedQueue<T> concurrentLinkedQueue = queue(tClass);
        try {
            T t = concurrentLinkedQueue.poll();
            if(t == null) {
                try {
                    t = tClass.newInstance();
                } catch (InstantiationException | IllegalAccessException ignore) {}
            }
            return t;
        } finally {
            cacheEntityCreator.fill(tClass, this, this.cacheSize);
        }
    }

    @Override
    public <T extends Entity> void add(T t) {
        ConcurrentLinkedQueue<T> concurrentLinkedQueue = (ConcurrentLinkedQueue<T>) queue(t.getClass());
        concurrentLinkedQueue.add(t);
    }

    @Override
    public <T extends Entity> int sizeOf(Class<T> tClass) {
        return queue(tClass).size();
    }

    private <T extends Entity> ConcurrentLinkedQueue<T> queue(Class<T> tClass) {
        return cache.computeIfAbsent(tClass, ConcurrentLinkedQueue -> new ConcurrentLinkedQueue<>());
    }
}
