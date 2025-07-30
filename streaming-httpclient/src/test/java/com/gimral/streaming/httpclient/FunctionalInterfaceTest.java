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

public class FunctionalInterfaceTest {

  @RegisterExtension
  public static final MiniClusterExtension MINI_CLUSTER =
      new MiniClusterExtension(
          new MiniClusterResourceConfiguration.Builder()
              .setNumberTaskManagers(1)
              .setNumberSlotsPerTaskManager(2)
              .build());

  @Test
  public void testFunctionalInterfaces() throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    DataStream<String> ds = env.fromData(Arrays.asList("1", "2", "3")).setParallelism(1);

    HttpClientEnrichmentFunction<String, String> enrichmentFunction =
        new HttpClientEnrichmentFunction<>(
            (requestBuilder, input) -> requestBuilder.url("https://api.restful-api.dev/objects"),
            (response, input) -> {
              // Assuming the response body is a String for simplicity
              System.out.println("Response received: " + response.string());
              return input;
            },
            () -> "default_value");

    DataStream<String> enriched =
        AsyncDataStream.unorderedWait(ds, enrichmentFunction, 1000, TimeUnit.MILLISECONDS, 100)
            .returns(String.class);

    AsyncRetryStrategy asyncRetryStrategy =
        new AsyncRetryStrategies.FixedDelayRetryStrategyBuilder(
                3, 100L) // maxAttempts=3, fixedDelay=100ms
            .ifResult(RetryPredicates.EMPTY_RESULT_PREDICATE)
            .ifException(RetryPredicates.HAS_EXCEPTION_PREDICATE)
            .build();

    env.execute();
  }
}
