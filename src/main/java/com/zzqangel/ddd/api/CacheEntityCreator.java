package com.zzqangel.ddd.api;

import com.zzqangel.ddd.model.Entity;

public interface CacheEntityCreator {
    public <T extends Entity> void fill(Class<T> tClass, EntityCache entityCache);
}
