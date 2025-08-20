package com.gimral;

import com.gimral.streaming.httpclient.HttpClientEnricher;
import com.gimral.streaming.httpclient.HttpClientEnricherRequestSetup;
import com.gimral.streaming.httpclient.HttpClientEnricherSuccessHandler;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;

import java.util.Collections;

public abstract class CacheEnricher<I, O, T> extends RichAsyncFunction<I, O> {

    private final CacheEnricherKeyProvider<I> keyProvider;
    private final CacheEnricherSuccessHandler<I, O, T> successCallback;
    private final CacheEnricherErrorHandler<I, O> errorCallback;
    private HttpClientEnricher<I, O> fallbackEnricher;


    public CacheEnricher(CacheEnricherKeyProvider<I> keyProvider,
                         CacheEnricherSuccessHandler<I, O, T> successCallback,
                         CacheEnricherErrorHandler<I, O> errorCallback) {
        this.keyProvider = keyProvider;
        this.successCallback = successCallback;
        this.errorCallback = errorCallback;
        this.fallbackEnricher = null;
    }

    public void setupFallbackEnricher(HttpClientEnricherRequestSetup<I> requestSetup,
                                      HttpClientEnricherSuccessHandler<I, O> successCallbackFallback) {
        this.fallbackEnricher = new HttpClientEnricher<>(
                requestSetup,
                successCallbackFallback,
                errorCallback::onError
        );
    }


    @Override
    public void asyncInvoke(I input, ResultFuture<O> resultFuture) throws Exception {
        try {
            //perform cache operation here
            String key = keyProvider.getKey(input);
            T result = performCacheOperation(key);
            O output = successCallback.onSuccess(result, input);
            resultFuture.complete(Collections.singletonList(output));
        } catch (Exception e) {
            //Handle retries and then fallback
            //Call the httpClientEnricher
            if(fallbackEnricher != null) {
                fallbackEnricher.asyncInvoke(input, resultFuture);
            }
            //After all the retries, call the error callback

            O output = errorCallback.onError(input);
            resultFuture.complete(Collections.singletonList(output));
        }
    }

    public abstract T performCacheOperation(String key) throws Exception;

    @Override
    public void timeout(I input, ResultFuture<O> resultFuture) throws Exception {
        super.timeout(input, resultFuture);
    }
}
