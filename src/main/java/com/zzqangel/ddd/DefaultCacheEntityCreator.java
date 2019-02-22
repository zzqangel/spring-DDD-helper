package com.zzqangel.ddd;

import com.zzqangel.ddd.api.CacheEntityCreator;
import com.zzqangel.ddd.api.EntityCache;
import com.zzqangel.ddd.model.Entity;
import javafx.beans.binding.BooleanBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class DefaultCacheEntityCreator implements CacheEntityCreator {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final ConcurrentHashMap<Class, Boolean> fillMap = new ConcurrentHashMap<>();

    @Autowired
    private EntitySetter entitySetter;
    @Override
    public <T extends Entity> void fill(Class<T> tClass, EntityCache entityCache, int fillSize) {
        if(getFill(tClass)) return;
        fillMap.put(tClass, true);
        executorService.submit(() -> {
            try {
                while(entityCache.sizeOf(tClass) < fillSize) {
                    entityCache.add(entitySetter.setEntity(tClass.newInstance()));
                }
            } catch (IllegalAccessException | InstantiationException ignore) {

            } finally {
                fillMap.put(tClass, false);
            }
        });
    }

    private Boolean getFill(Class c) {
        return fillMap.computeIfAbsent(c, aClass -> false);
    }

}
