package com.gimral;

import com.gimral.streaming.httpclient.HttpClientEnricherRequestSetup;
import com.gimral.streaming.httpclient.HttpClientEnricherSuccessHandler;

public class AccountExtensionEnricher<I, O> extends RedisEnricher<I, O, AccountExtension> {

    public AccountExtensionEnricher(CacheEnricherKeyProvider<I> keyProvider,
                                    CacheEnricherSuccessHandler<I, O, AccountExtension> successCallback,
                                    CacheEnricherErrorHandler<I, O> errorCallback) {
        super(keyProvider, successCallback, errorCallback);
        setupFallbackEnricher(
                getFallbackRequestSetup(),
                getFallbackSuccessCallback()
        );
    }

    @Override
    public AccountExtension performCacheOperation(String key) throws Exception {
        return jedis.jsonGet(key, AccountExtension.class);
    }

    HttpClientEnricherSuccessHandler<I, O> getFallbackSuccessCallback() {
        return (response, input) -> null; // Default implementation
    }

    static <I> HttpClientEnricherRequestSetup<I> getFallbackRequestSetup() {
        return (requestBuilder, input) -> {}; // Default implementation
    }
}
