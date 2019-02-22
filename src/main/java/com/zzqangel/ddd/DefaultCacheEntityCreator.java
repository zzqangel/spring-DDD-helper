package com.zzqangel.ddd;

import com.zzqangel.ddd.api.CacheEntityCreator;
import com.zzqangel.ddd.api.EntityCache;
import com.zzqangel.ddd.model.Entity;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class DefaultCacheEntityCreator implements CacheEntityCreator {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    @Override
    public <T extends Entity> void fill(Class<T> tClass, EntityCache entityCache) {
        executorService.submit(() -> {

        });
    }
}
