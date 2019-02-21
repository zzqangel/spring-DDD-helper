package com.zzqangel.ddd;

import com.zzqangel.ddd.model.Entity;

/**
 * EntityFactory is the way to create model entity
 */
public class EntityFactory {

    private EntityFactory(){}

    public static <T extends Entity> T createEntity(Class<T> tClass) {
        return null;
    }
}
