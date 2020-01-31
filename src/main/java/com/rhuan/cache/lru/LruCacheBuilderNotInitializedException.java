package com.rhuan.cache.lru;

public class LruCacheBuilderNotInitializedException extends RuntimeException {

    public LruCacheBuilderNotInitializedException(String message) {
        super(message);
    }
}
