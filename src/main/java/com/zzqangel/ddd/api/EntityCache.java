package com.zzqangel.ddd.api;

import com.zzqangel.ddd.model.Entity;

public interface EntityCache {

    <T extends Entity> T poll(Class<T> tClass);
}
