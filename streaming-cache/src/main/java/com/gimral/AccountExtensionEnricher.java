package com.gimral;

import com.gimral.streaming.httpclient.HttpClientEnricherErrorHandler;
import com.gimral.streaming.httpclient.HttpClientEnricherRequestSetup;
import com.gimral.streaming.httpclient.HttpClientEnricherSuccessHandler;

public class AccountExtensionEnricher<I, O> extends CacheEnricher<I, O, AccountExtension> {

    public AccountExtensionEnricher(CacheEnricherSuccessHandler<I, O, AccountExtension> successCallback,
                                    CacheEnricherErrorHandler<I, O> errorCallback) {
        super(successCallback, errorCallback);
        setupFallbackEnricher(
                getFallbackRequestSetup(),
                getFallbackSuccessCallback(),
                getFallbackErrorCallback()
        );
    }

    HttpClientEnricherSuccessHandler<I, O> getFallbackSuccessCallback() {
        return (input, accountExtension) -> null; // Default implementation
    }
    static <I, O> HttpClientEnricherErrorHandler<I, O> getFallbackErrorCallback() {
        return (input) -> null; // Default implementation
    }
    static <I> HttpClientEnricherRequestSetup<I> getFallbackRequestSetup() {
        return (requestBuilder, input) -> {}; // Default implementation
    }
}
