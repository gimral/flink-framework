package com.gimral;

@FunctionalInterface
public interface CacheMapper<I,O,C> {
    O map(I value,C cacheObject ) throws Exception;
}
