package com.rhuan.cache.lru;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LruCacheImpl implements LruCache {

    private int maxSize;
    private Map<Object, Object> cacheMap;
    private LinkedList<Object> cacheIndex;

    LruCacheImpl(int maxSize) {
        this.maxSize = maxSize;
        this.cacheMap = new HashMap<>();
        this.cacheIndex = new LinkedList<>();
    }

    @Override
    public Object get(Object key) {
        if (cacheMap.containsKey(key)) {
            renewObjectInTheIndex(key);
            return cacheMap.get(key);
        }

        return null;
    }

    @Override
    public void put(Object key, Object value) {
        cacheMap.put(key, value);

        synchronized (cacheIndex) {
            if (cacheIndex.lastIndexOf(key) < 0) {
                cacheIndex.addFirst(key);
            } else {
                renewObjectInTheIndex(key);
            }
        }

        ensureSizeConsistency();
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }

    private void renewObjectInTheIndex(Object key) {
        cacheIndex.push(cacheIndex.remove(cacheIndex.lastIndexOf(key)));
    }

    private void ensureSizeConsistency() {
        if (cacheMap.size() > maxSize) {
            purgeOldestObject();
        }
    }

    private void purgeOldestObject() {
        synchronized (cacheIndex) {
            cacheMap.remove(cacheIndex.removeLast());
        }
    }
}
