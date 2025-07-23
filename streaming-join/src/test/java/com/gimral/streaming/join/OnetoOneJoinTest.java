package com.gimral.streaming.join;

import java.util.Arrays;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.jupiter.api.Test;

public class OnetoOneJoinTest {
  @Test
  public void testInnerJoin() {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    DataStream<Integer> ds = env.fromData(Arrays.asList(1, 2, 3, 4)).setParallelism(1);

    KeyedStream<Integer, Integer> left = ds.keyBy(d -> d);
    KeyedStream<Integer, Integer> right = ds.keyBy(d -> d);
  }
}
