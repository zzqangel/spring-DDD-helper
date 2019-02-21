package com.zzqangel.ddd.config;

public class EntityCacheConfiguration {

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    private int cacheSize;
}
