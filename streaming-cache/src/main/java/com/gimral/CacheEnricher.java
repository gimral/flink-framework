package com.gimral;

import com.gimral.streaming.httpclient.HttpClientEnricher;
import com.gimral.streaming.httpclient.HttpClientEnricherErrorHandler;
import com.gimral.streaming.httpclient.HttpClientEnricherRequestSetup;
import com.gimral.streaming.httpclient.HttpClientEnricherSuccessHandler;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;

import java.util.Collections;

public class CacheEnricher<I, O, T> extends RichAsyncFunction<I, O> {

    private final CacheEnricherSuccessHandler<I, O, T> successCallback;
    private final CacheEnricherErrorHandler<I, O> errorCallback;
    private HttpClientEnricher<I, O> fallbackEnricher;


    public CacheEnricher(CacheEnricherSuccessHandler<I, O, T> successCallback, CacheEnricherErrorHandler<I, O> errorCallback) {
        this.successCallback = successCallback;
        this.errorCallback = errorCallback;
        this.fallbackEnricher = null;
    }

    public void setupFallbackEnricher(HttpClientEnricherRequestSetup<I> requestSetup,
                                      HttpClientEnricherSuccessHandler<I, O> successCallbackFallback,
                                      HttpClientEnricherErrorHandler<I, O> errorCallbackFallback) {
        this.fallbackEnricher = new HttpClientEnricher<>(
                requestSetup,
                successCallbackFallback,
                errorCallbackFallback
        );
    }

    public CacheEnricher(CacheEnricherSuccessHandler<I, O, T> successCallback, CacheEnricherErrorHandler<I, O> errorCallback,
                         HttpClientEnricherRequestSetup<I> requestSetup,
                         HttpClientEnricherSuccessHandler<I, O> successCallbackFallback,
                         HttpClientEnricherErrorHandler<I, O> errorCallbackFallback) {
        this.successCallback = successCallback;
        this.errorCallback = errorCallback;
        this.fallbackEnricher = new HttpClientEnricher<>(
                requestSetup,
                successCallbackFallback,
                errorCallbackFallback
        );
    }


    @Override
    public void asyncInvoke(I input, ResultFuture<O> resultFuture) throws Exception {
        try {
            //perform cache operation here
            //T result = performCacheOperation(input);
            T result = null;
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

    @Override
    public void timeout(I input, ResultFuture<O> resultFuture) throws Exception {
        super.timeout(input, resultFuture);
    }
}
