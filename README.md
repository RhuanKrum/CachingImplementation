# CachingImplementation
 
Caching implementation project. The idea is to implement some of the most used caching algorithms.

## LRU Cache (Least Recently Used)

This is a LRU caching implementation that can perform well in environments that use threading. The implementation is using HashMap to hold the cache keys and values and a LinkedList to hold the index of each key so they can be purged once the max size is reached. 

This implementation is completed and can be found at `main/java/com/rhuan/cache/lru`. 

There are several test cases implemented as a proof of usability at `test/java/com/rhuan/cache/lru`.
