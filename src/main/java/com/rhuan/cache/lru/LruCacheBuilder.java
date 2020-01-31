package com.rhuan.cache.lru;

public final class LruCacheBuilder {

    private int cacheMaxSize;

    public LruCacheBuilder() {
        cacheMaxSize = 10;
    }

    public LruCacheBuilder withMaxSize(int size) {
        cacheMaxSize = size;
        return this;
    }

    public LruCache build() {
        LruCache cache = new LruCacheImpl(cacheMaxSize);
        return cache;
    }
}
