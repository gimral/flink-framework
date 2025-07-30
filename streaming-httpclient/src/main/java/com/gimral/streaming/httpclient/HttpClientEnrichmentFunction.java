package com.gimral.streaming.httpclient;

import java.io.IOException;
import java.util.Collections;

import okhttp3.*;
import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.jetbrains.annotations.NotNull;

public class HttpClientEnrichmentFunction<I, O> extends RichAsyncFunction<I, O> {
  private transient OkHttpClient httpClient;

  private final RequestSetup<I> requestSetup;
  private final SuccessHandler<I, O> successCallback;
  private final ErrorHandler<O> errorCallback;

  public HttpClientEnrichmentFunction(
      RequestSetup<I> requestSetup, SuccessHandler<I, O> successCallback) {
    this(requestSetup, successCallback, () -> null);
  }

  public HttpClientEnrichmentFunction(
      RequestSetup<I> requestSetup,
      SuccessHandler<I, O> successCallback,
      ErrorHandler<O> errorCallback) {
    this.requestSetup = requestSetup;
    this.successCallback = successCallback;
    this.errorCallback = errorCallback;
  }

  @Override
  public void open(OpenContext openContext) throws Exception {
    httpClient = new OkHttpClient.Builder().build();
  }

  @Override
  public void close() throws Exception {
    // Clean up the HTTP client
    if (httpClient != null) {
      httpClient.dispatcher().executorService().shutdown();
      httpClient.connectionPool().evictAll();
    }
  }

  @Override
  public void asyncInvoke(I input, ResultFuture<O> resultFuture) throws Exception {
    Request.Builder requestBuilder = new Request.Builder();
    requestSetup.setup(requestBuilder, input);

    Call call = httpClient.newCall(requestBuilder.build());

    call.enqueue(new Callback() {
      @Override
      public void onFailure(@NotNull Call call, @NotNull IOException e) {
        // Handle failure (e.g., network error)
        resultFuture.completeExceptionally(e);
      }

      @Override
      public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if (response.isSuccessful() && response.body() != null) {
          // Successful response, forward the result
          O output = successCallback.onSuccess(response.body(), input);
          resultFuture.complete(Collections.singleton(output));
        } else {
          // Handle non-successful response (e.g., 4xx, 5xx errors). Empty result may get retried.
          resultFuture.complete(Collections.emptyList());
        }
        response.close();
      }
    });
  }

  @Override
  public void timeout(I input, ResultFuture<O> resultFuture) {
    O output = errorCallback.onError();
    resultFuture.complete(Collections.singleton(output));
  }
}
