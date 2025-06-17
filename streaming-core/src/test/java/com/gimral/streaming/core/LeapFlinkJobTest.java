package com.gimral.streaming.core;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.test.junit5.MiniClusterExtension;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.gimral.streaming.core.datastream.LeapDataStream;
import com.gimral.streaming.core.functions.LeapMapFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LeapFlinkJobTest {
    @RegisterExtension
    public static final MiniClusterExtension MINI_CLUSTER = new MiniClusterExtension(
            new MiniClusterResourceConfiguration.Builder()
                    .setNumberTaskManagers(1)
                    .setNumberSlotsPerTaskManager(2)
                    .build());

    public static class DummyLeapFlinkJob extends LeapFlinkJob {
        @Override
        public void build(StreamExecutionEnvironment env) {
            // No-op for test
        }
    }

    public static class FilteringLeapFlinkJob extends LeapFlinkJob {
        private List<Integer> result;

        @Override
        public void build(StreamExecutionEnvironment env) {
            @SuppressWarnings("deprecation")
            DataStream<Integer> dataStream = env.fromData(Arrays.asList(1, 2, 3, 4, 5)).setParallelism(1);
            LeapDataStream<Integer> ds = new LeapDataStream<>(dataStream);
            ds = ds.map(new LeapMapFunction<Integer, Integer>() {
                @Override
                public Integer leapMap(Integer value) {
                    // Custom mapping logic
                    return value;
                }
            });
            ds = ds.map(i -> i * 2);
            // DataStream<Integer> input = env.fromCollection(Arrays.asList(1, 2, 3, 4, 5));
            // DataStream<Integer> filtered = input.filter((FilterFunction<Integer>) value
            // -> value % 2 == 0);
            try {
                Iterator<Integer> it = ds.getDelegate().executeAndCollect();
                result = new java.util.ArrayList<>();
                while (it.hasNext()) {
                    result.add(it.next());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public List<Integer> getResult() {
            return result;
        }
    }

    @Test
    public void testBuildMethodRunsWithoutException() {
        DummyLeapFlinkJob job = new DummyLeapFlinkJob();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        assertDoesNotThrow(() -> job.build(env));
    }

    @Test
    public void testFilteringJob() {
        FilteringLeapFlinkJob job = new FilteringLeapFlinkJob();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        job.build(env);
        // Only even numbers should remain
        assertEquals(Arrays.asList(2, 4, 6, 8, 10), job.getResult());
    }
}
