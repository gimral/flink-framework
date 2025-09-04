// package com.gimral.streaming.core.job;
//
// import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//
// import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
// import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
// import org.apache.flink.test.junit5.MiniClusterExtension;
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.RegisterExtension;
//
// public class MapperFlinkJobTest {
//    @RegisterExtension
//    public static final MiniClusterExtension MINI_CLUSTER =
//            new MiniClusterExtension(
//                    new MiniClusterResourceConfiguration.Builder()
//                            .setNumberTaskManagers(1)
//                            .setNumberSlotsPerTaskManager(2)
//                            .build());
//
//    @Test
//    public void testBuildMethodRunsWithoutException() {
//        DummyLeapFlinkJob job = new DummyLeapFlinkJob();
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        assertDoesNotThrow(() -> job.build(env));
//    }
//
//    @Test
//    public void testFilteringJob() {
//        MapperFlinkJob job = new MapperFlinkJob();
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        job.build(env);
//        // Only even numbers should remain
//        Assertions.assertEquals(4, job.getResult().size());
//    }
// }
