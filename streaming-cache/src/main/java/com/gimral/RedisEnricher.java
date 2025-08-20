package com.gimral;

import org.apache.flink.api.common.functions.OpenContext;
import redis.clients.jedis.UnifiedJedis;

public abstract class RedisEnricher<I, O, T> extends CacheEnricher<I, O, T>{
    protected transient UnifiedJedis jedis;

    public RedisEnricher(CacheEnricherKeyProvider<I> keyProvider,
                         CacheEnricherSuccessHandler<I, O, T> successCallback,
                         CacheEnricherErrorHandler<I, O> errorCallback) {
        super(keyProvider, successCallback, errorCallback);
    }


    @Override
    public void open(OpenContext openContext) throws Exception {
        // Initialize Redis client or connection here
        jedis = new UnifiedJedis("localhost:6379");
    }

    @Override
    public void close() throws Exception {
        // Clean up the Redis client
        if (jedis != null) {
            jedis.close();
        }
    }
}
