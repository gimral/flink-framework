package com.gimral.streaming.runtime.aop; /// * (C) 2025 */
// package com.gimral.streaming.core.aop;
//
// import static org.junit.jupiter.api.Assertions.*;
//
// import com.gimral.streaming.core.aop.advise.TracingAspect;
// import com.gimral.streaming.core.helpers.function.LeapRecordFilterFunction;
// import com.gimral.streaming.core.helpers.function.LeapRecordFlatMapFunction;
// import com.gimral.streaming.core.helpers.function.LeapRecordKeyedCoProcessFunction;
// import com.gimral.streaming.core.helpers.function.LeapRecordMapFunction;
// import com.gimral.streaming.core.model.LeapEventIntRecord;
// import com.gimral.streaming.core.model.LeapRecordConstants;
// import io.opentelemetry.api.common.AttributeKey;
// import io.opentelemetry.sdk.testing.junit5.OpenTelemetryExtension;
// import io.opentelemetry.sdk.trace.data.SpanData;
// import java.util.List;
// import org.apache.flink.api.common.functions.FilterFunction;
// import org.apache.flink.api.common.functions.FlatMapFunction;
// import org.apache.flink.api.common.functions.MapFunction;
// import org.apache.flink.api.common.typeinfo.TypeInformation;
// import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
// import org.apache.flink.streaming.api.operators.co.KeyedCoProcessOperator;
// import org.apache.flink.streaming.api.operators.*;
// import org.apache.flink.streaming.util.KeyedTwoInputStreamOperatorTestHarness;
// import org.apache.flink.streaming.util.OneInputStreamOperatorTestHarness;
// import org.apache.flink.streaming.util.TwoInputStreamOperatorTestHarness;
// import org.apache.flink.util.Collector;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.RegisterExtension;
//
// public class TracingAspectTest {
//    @RegisterExtension
//    private static final OpenTelemetryExtension otelTesting = OpenTelemetryExtension.create();
//
//    @BeforeAll
//    public static void setup() {
//        TracingAspect.setTracer(otelTesting.getOpenTelemetry().getTracer("flink-tracer"));
//    }
//
//    @Test
//    void testWithConcreteMapFunction() throws Exception {
//        testMapFunction(new LeapRecordMapFunction());
//    }
//
//    @Test
//    void testWithLambdaMapFunction() throws Exception {
//        testMapFunction(value -> value);
//    }
//
//    @Test
//    void testWithAnonymousMapFunction() throws Exception {
//        testMapFunction(
//                new MapFunction<LeapEventIntRecord, LeapEventIntRecord>() {
//                    @Override
//                    public LeapEventIntRecord map(LeapEventIntRecord value) throws Exception {
//                        return value;
//                    }
//                });
//    }
//
//    @Test
//    void testWithConcreteFilterFunction() throws Exception {
//        testFilterFunction(new LeapRecordFilterFunction());
//    }
//
//    @Test
//    void testWithLambdaFilterFunction() throws Exception {
//        testFilterFunction((FilterFunction<LeapEventIntRecord>) value -> true);
//    }
//
//    @Test
//    void testWithAnonymousFilterFunction() throws Exception {
//        testFilterFunction(
//                new FilterFunction<LeapEventIntRecord>() {
//                    @Override
//                    public boolean filter(LeapEventIntRecord value) throws Exception {
//                        return true;
//                    }
//                });
//    }
//
//    @Test
//    void testWithConcreteFlatMapFunction() throws Exception {
//        testFlatMapFunction(new LeapRecordFlatMapFunction());
//    }
//
//    @Test
//    void testWithLambdaFlatMapFunction() throws Exception {
//        testFlatMapFunction(
//                (value, collector) -> {
//                    collector.collect(value);
//                });
//    }
//
//    @Test
//    void testWithAnonymousFlatMapFunction() throws Exception {
//        testFlatMapFunction(
//                new FlatMapFunction<LeapEventIntRecord, LeapEventIntRecord>() {
//                    @Override
//                    public void flatMap(
//                            LeapEventIntRecord value, Collector<LeapEventIntRecord> collector)
//                            throws Exception {
//                        collector.collect(value);
//                    }
//                });
//    }
//
//    @Test
//    public <T> void testWithConcreteKeyedCoProcessFunction() throws Exception {
//        testKeyedCoProcessFunction(new LeapRecordKeyedCoProcessFunction());
//    }
//
//    private void testMapFunction(MapFunction<LeapEventIntRecord, LeapEventIntRecord> function)
//            throws Exception {
//        testOperator(new StreamMap<>(function), LeapEventIntRecord.getTestRecord(1, 1, "1",
// "map"));
//    }
//
//    private void testFilterFunction(FilterFunction<LeapEventIntRecord> function) throws Exception
// {
//        testOperator(
//                new StreamFilter<>(function),
//                LeapEventIntRecord.getTestRecord(1, 1, "1", "filter"));
//    }
//
//    private void testFlatMapFunction(
//            FlatMapFunction<LeapEventIntRecord, LeapEventIntRecord> function) throws Exception {
//        testOperator(
//                new StreamFlatMap<>(function), LeapEventIntRecord.getTestRecord(1, 1, "1",
// "map"));
//    }
//
//    private void testKeyedCoProcessFunction(
//            KeyedCoProcessFunction<
//                            String, LeapEventIntRecord, LeapEventIntRecord, LeapEventIntRecord>
//                    function)
//            throws Exception {
//        testKeyedTwoInputOperator(
//                new KeyedCoProcessOperator<>(function),
//                LeapEventIntRecord.getTestRecord(1, 1, "1", "map"),
//                LeapEventIntRecord.getTestRecord(1, 1, "2", "map"));
//    }
//
//    private <T> void testOperator(OneInputStreamOperator<T, T> operator, T testRecord)
//            throws Exception {
//
//        try (OneInputStreamOperatorTestHarness<T, T> harness =
//                new OneInputStreamOperatorTestHarness<>(operator)) {
//            harness.open();
//            harness.processElement(testRecord, System.currentTimeMillis());
//        }
//
//        List<SpanData> spans = otelTesting.getSpans();
//        assertEquals(1, spans.size(), "One span should be created");
//        SpanData span = spans.get(0);
//        assertTrue(span.getName().contains("process by"), "Span name should match");
//        assertNotNull(span.getSpanId(), "Span ID should be generated");
//        assertEquals(
//                "1",
//                span.getAttributes().get(AttributeKey.stringKey(LeapRecordConstants.URC)),
//                "URC should match");
//    }
//
//    private <T> void testKeyedTwoInputOperator(
//            TwoInputStreamOperator<T, T, T> operator, T firstRecord, T secondRecord)
//            throws Exception {
//        try (KeyedTwoInputStreamOperatorTestHarness<String, T, T, T> harness =
//                new KeyedTwoInputStreamOperatorTestHarness<>(
//                        operator, v -> "1", v -> "1", TypeInformation.of(String.class))) {
//            harness.setup();
//            harness.open();
//            harness.processElement1(firstRecord, System.currentTimeMillis());
//            harness.processElement2(secondRecord, System.currentTimeMillis());
//        }
//    }
//
//    private <T> void testTwoInputOperator(
//            TwoInputStreamOperator<T, T, T> operator, T firstRecord, T secondRecord)
//            throws Exception {
//        try (TwoInputStreamOperatorTestHarness<T, T, T> harness =
//                new TwoInputStreamOperatorTestHarness<>(operator)) {
//            harness.setup();
//            harness.open();
//            harness.processElement1(firstRecord, System.currentTimeMillis());
//            harness.processElement2(secondRecord, System.currentTimeMillis());
//        }
//    }
// }
