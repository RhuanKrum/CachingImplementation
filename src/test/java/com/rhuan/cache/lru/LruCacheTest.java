package com.rhuan.cache.lru;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class LruCacheTest {

    @Test
    public void LruCacheBuilder_should_build_with_no_settings() {
        LruCache cache = new LruCacheBuilder().build();

        assertTrue( cache instanceof LruCacheImpl );
    }

    @Test
    public void LruCacheBuilder_should_build_with_max_size() {
        int maxSize = 100;
        LruCache cache = new LruCacheBuilder().withMaxSize(maxSize).build();

        assertTrue( cache instanceof LruCacheImpl);
        assertTrue(cache.getMaxSize() == maxSize);
    }

    @Test
    public void LruCache_should_accept_new_items_in_cache() {
        LruCache cache = new LruCacheBuilder().build();
        cache.put("Key", "Value");
        cache.put("Key2", "Value2");

        assertTrue(cache.get("Key") != null);
        assertTrue(cache.get("Key").equals("Value"));
        assertTrue(cache.get("Key2") != null);
        assertTrue(cache.get("Key2").equals("Value2"));
    }

    @Test
    public void LruCache_should_not_exceed_max_size() {
        LruCache cache = new LruCacheBuilder().withMaxSize(2).build();
        cache.put("Key", "Value");
        cache.put("Key2", "Value2");
        cache.put("Key3", "Value3");

        assertTrue(cache.get("Key") == null);
    }

    @Test
    public void LruCache_should_replace_values_with_the_same_keys() {
        LruCache cache = new LruCacheBuilder().withMaxSize(2).build();
        cache.put("Key", "Value");
        cache.put("Key2", "Value");
        cache.put("Key2", "Value2");
        cache.put("Key2", "Value3");

        assertTrue(cache.get("Key") != null);
        assertTrue(cache.get("Key2") != null);
        assertTrue(cache.get("Key2").equals("Value3"));
    }

    @Test
    public void LruCache_should_renew_object_in_cache() {
        LruCache cache = new LruCacheBuilder().withMaxSize(2).build();
        cache.put("Key", "Value");
        cache.put("Key2", "Value2");
        cache.get("Key");
        cache.put("Key3", "Value3");
        cache.get("Key");
        cache.put("Key4", "Value4");

        assertTrue(cache.get("Key") != null);
        assertTrue(cache.get("Key2") == null);
        assertTrue(cache.get("Key3") == null);
    }

    @Test
    public void LruCache_should_accept_multiple_object_types() {
        LruCache cache = new LruCacheBuilder().withMaxSize(10).build();
        cache.put("Key", "Value");
        cache.put(0, 0);
        cache.put(0L, 0L);
        cache.put(new ArrayList<String>(), new ArrayList<Integer>());
        cache.put("Key2", 0);
        cache.put(1, "Key 3");
        cache.put(1, 0);
    }

    @Test
    public void LruCache_should_work_with_big_cache() {
        int maxSize = 50_000;
        LruCache cache = new LruCacheBuilder().withMaxSize(maxSize).build();
        runLoopToPutIntegerValuesIntoCache(cache, maxSize + 100);

        // At this point the cache should be holding items from 101 - 50_100
        assertTrue(cache.get(100) == null);
        assertTrue(cache.get(101) != null);
        assertTrue(cache.get(maxSize + 100) != null);
    }

    @Test
    public void LruCache_should_be_quick_to_add_new_items_into_big_cache() {
        int maxSize = 50_000;
        LruCache cache = new LruCacheBuilder().withMaxSize(maxSize).build();
        runLoopToPutIntegerValuesIntoCache(cache, maxSize);

        Long startTime = System.currentTimeMillis();
        cache.put(new Object(), new Object());
        Long finishTime = System.currentTimeMillis();

        Long totalTime = finishTime - startTime;

        assertTrue(totalTime < 10);
    }

    @Test
    public void LruCache_should_handle_multi_threading() {
        int maxSize = 10_000;
        LruCache cache = new LruCacheBuilder().withMaxSize(maxSize).build();

        List<Thread> threads = new ArrayList<>();
        threads.add(new Thread(() -> runLoopToPutIntegerValuesIntoCache(cache, maxSize)));
        threads.add(new Thread(() -> runLoopToPutIntegerValuesIntoCache(cache, maxSize)));
        threads.add(new Thread(() -> runLoopToPutIntegerValuesIntoCache(cache, maxSize)));
        threads.add(new Thread(() -> runLoopToPutIntegerValuesIntoCache(cache, maxSize)));
        threads.add(new Thread(() -> runLoopToPutIntegerValuesIntoCache(cache, maxSize)));
        threads.add(new Thread(() -> runLoopToPutIntegerValuesIntoCache(cache, maxSize)));
        threads.add(new Thread(() -> runLoopToPutIntegerValuesIntoCache(cache, maxSize)));
        threads.add(new Thread(() -> runLoopToPutIntegerValuesIntoCache(cache, maxSize)));
        threads.add(new Thread(() -> runLoopToPutIntegerValuesIntoCache(cache, maxSize)));
        threads.add(new Thread(() -> runLoopToPutIntegerValuesIntoCache(cache, maxSize)));
        threads.add(new Thread(() -> runLoopToPutIntegerValuesIntoCache(cache, maxSize)));
        threads.add(new Thread(() -> runLoopToPutIntegerValuesIntoCache(cache, maxSize)));
        threads.add(new Thread(() -> runLoopToPutIntegerValuesIntoCache(cache, maxSize)));
        threads.add(new Thread(() -> runLoopToPutIntegerValuesIntoCache(cache, maxSize)));
        threads.add(new Thread(() -> runLoopToPutIntegerValuesIntoCache(cache, maxSize)));

        threads.forEach(Thread::start);

        while (threads.stream().anyMatch(Thread::isAlive)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                fail();
            }
        }
    }

    private void runLoopToPutIntegerValuesIntoCache(LruCache cache, int maxSize) {
        for (int i = 0; i <= maxSize; i++) {
            cache.put(i, i);
        }
    }
}
