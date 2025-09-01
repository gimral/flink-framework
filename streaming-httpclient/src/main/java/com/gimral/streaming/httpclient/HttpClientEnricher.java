package com.gimral.streaming.httpclient;

import io.github.resilience4j.retry.Retry;
import java.io.IOException;
import java.util.Collections;
import okhttp3.*;
import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.jetbrains.annotations.NotNull;

public class HttpClientEnricher<I, O> extends RichAsyncFunction<I, O> {
  private transient OkHttpClient httpClient;

  private final HttpClientEnricherRequestSetup<I> requestSetup;
  private final HttpClientEnricherSuccessHandler<I, O> successCallback;
  private final HttpClientEnricherErrorHandler<I, O> errorCallback;
  private final DefaultHeaderAssigner<I> defaultHeaderAssigner;

  public HttpClientEnricher(
      HttpClientEnricherRequestSetup<I> requestSetup,
      HttpClientEnricherSuccessHandler<I, O> successCallback) {
    this(requestSetup, successCallback, (input) -> null);
  }

  public HttpClientEnricher(
      HttpClientEnricherRequestSetup<I> requestSetup,
      HttpClientEnricherSuccessHandler<I, O> successCallback,
      HttpClientEnricherErrorHandler<I, O> errorCallback) {
    this.requestSetup = requestSetup;
    this.successCallback = successCallback;
    this.errorCallback = errorCallback;
    this.defaultHeaderAssigner = new DefaultHeaderAssigner<>();
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

    Retry retry = Retry.ofDefaults("http-call-retry");
    retry.getEventPublisher().onRetry(e -> e.getLastThrowable());

    Request.Builder requestBuilder = new Request.Builder();
    requestSetup.setup(requestBuilder, input);
    defaultHeaderAssigner.applyDefaultHeaders(requestBuilder, input);

    Call call = httpClient.newCall(requestBuilder.build());

    call.enqueue(
        new Callback() {
          @Override
          public void onFailure(@NotNull Call call, @NotNull IOException e) {
            // Handle failure (e.g., network error)
            System.out.println("Failure");
            resultFuture.complete(Collections.emptyList());
          }

          @Override
          public void onResponse(@NotNull Call call, @NotNull Response response)
              throws IOException {
            if (response.isSuccessful() && response.body() != null) {
              // Successful response, forward the result
              O output = successCallback.onSuccess(response.body(), input);
              resultFuture.complete(Collections.singleton(output));
            } else {
              // Handle non-successful response (e.g., 4xx, 5xx errors). Empty result may get
              // retried.
              System.out.println("Response not successful: " + response.code());
              resultFuture.complete(Collections.emptyList());
            }
            response.close();
          }
        });
  }

  @Override
  public void timeout(I input, ResultFuture<O> resultFuture) {
    System.out.println("Timeout occurred for input: " + input);
    O output = errorCallback.onError(input);
    resultFuture.complete(Collections.singleton(output));
  }
}
