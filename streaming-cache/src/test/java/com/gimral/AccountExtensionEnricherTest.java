//package com.gimral;
//
//
//import com.gimral.streaming.core.model.LeapRecord;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//
//public class AccountExtensionEnricherTest {
//
//    @Test
//    public void testMapFunction() throws Exception {
//
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//
//        DataStream<LeapRecord<Integer>> ds = env.fromData(Arrays.asList(
//                new LeapRecord<>(1,1,"1","map"),
//                new LeapRecord<>(2,1,"2","map"),
//                new LeapRecord<>(3,1,"3","map"),
//                new LeapRecord<>(4,1,"4","map")
//        )).setParallelism(1);
//
//
//
//        AccountExtensionEnricher
//                .builder()
//                .withoutFallBack()
//                .withKeySelector()
//                .withMapper()
//                .build();
//
//
//        ds.map(new AccountExtensionEnricher<LeapRecord<Integer>, Object>(
//                new CacheMapper<LeapRecord<Integer>, Object, String>() {
//                    @Override
//                    public Object map(LeapRecord<Integer> value, String cacheObject) throws Exception {
//                        return null;
//                    }
//                }
//        ))
//
//        CacheMapper<String, String, String> mapper = (value, cacheObject) -> {
//            // Simulate some mapping logic
//            return "Mapped: " + value + " with cache: " + cacheObject;
//        };
//
//        AccountExtensionEnricher<String, String> enricher = new AccountExtensionEnricher<>(mapper);
//        String result = enricher.map("TestValue");
//
//        // Assert the result
//        assert result.equals("Mapped: TestValue with cache: Enriching value: TestValue");
//    }
//}
