package com.gimral.streaming.httpclient;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.async.AsyncRetryStrategy;
import org.apache.flink.streaming.util.retryable.AsyncRetryStrategies;
import org.apache.flink.streaming.util.retryable.RetryPredicates;
import org.apache.flink.test.junit5.MiniClusterExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class HttpClientEnrichmentFunctionTest {

//  @RegisterExtension
//  public static final MiniClusterExtension MINI_CLUSTER =
//      new MiniClusterExtension(
//          new MiniClusterResourceConfiguration.Builder()
//              .setNumberTaskManagers(1)
//              .setNumberSlotsPerTaskManager(2)
//              .build());

  @Test
  public void testFunctionalInterfaces() throws Exception {

    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    DataStream<String> ds = env.fromData(Arrays.asList("1", "2", "3")).setParallelism(1);

    HttpClientEnrichmentFunction<String, String> enrichmentFunction =
        new HttpClientEnrichmentFunction<>(
            (requestBuilder, input) -> requestBuilder.url("https://apiy.restful-api.dev/objects"),
            (response, input) -> {
              // Assuming the response body is a String for simplicity
              System.out.println("Response received: " + response.string());
              return input;
            },
            () -> "default_value");

    //noinspection unchecked
    AsyncRetryStrategy<String> retryStrategy =
        new AsyncRetryStrategies.FixedDelayRetryStrategyBuilder<String>(
                10, 1000L) // maxAttempts=3, fixedDelay=1000ms
            .ifResult(RetryPredicates.EMPTY_RESULT_PREDICATE)
            .ifException(RetryPredicates.HAS_EXCEPTION_PREDICATE)
            .build();

    DataStream<String> enriched =
        AsyncDataStream.unorderedWaitWithRetry(
                ds, enrichmentFunction, 5000L, TimeUnit.MILLISECONDS, 10, retryStrategy)
            .returns(String.class);

//    enriched
//        .executeAndCollect()
//        .forEachRemaining(result -> System.out.println("Enriched result: " + result));

        env.executeAsync();

    Thread.sleep(6000);
  }
}
