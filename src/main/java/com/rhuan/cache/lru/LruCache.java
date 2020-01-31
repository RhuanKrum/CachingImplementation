package com.rhuan.cache.lru;

public interface LruCache {

    Object get(Object key);

    void put(Object key, Object value);

    int getMaxSize();
}
