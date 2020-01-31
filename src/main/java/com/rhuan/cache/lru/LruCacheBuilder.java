package com.rhuan.cache.lru;

public final class LruCacheBuilder {

    private int cacheMaxSize;

    private static LruCacheBuilder builder;

    private LruCacheBuilder() {
        cacheMaxSize = 10;
    }

    public static LruCacheBuilder createBuilder() {
        builder = new LruCacheBuilder();
        return builder;
    }

    public static LruCacheBuilder withMaxSize(int size) {
        checkBuilderInitialization();

        builder.cacheMaxSize = size;
        return builder;
    }

    public static LruCache build() {
        checkBuilderInitialization();

        LruCache cache = new LruCacheImpl(builder.cacheMaxSize);
        builder = null;
        return cache;
    }

    private static void checkBuilderInitialization() {
        if (builder == null) {
            throw new LruCacheBuilderNotInitializedException("Cache builder not initialized");
        }
    }
}
