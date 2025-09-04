package com.gimral.streaming.runtime.aop;

import static org.junit.jupiter.api.Assertions.*;

import com.gimral.streaming.core.model.LeapRecordConstants;
import com.gimral.streaming.runtime.function.*;
import com.gimral.streaming.runtime.model.LeapEventIntRecord;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Optional;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
import org.apache.flink.streaming.api.operators.co.KeyedCoProcessOperator;
import org.apache.flink.streaming.api.operators.*;
import org.apache.flink.streaming.runtime.streamrecord.StreamRecord;
import org.apache.flink.streaming.util.KeyedTwoInputStreamOperatorTestHarness;
import org.apache.flink.streaming.util.OneInputStreamOperatorTestHarness;
import org.apache.flink.streaming.util.TwoInputStreamOperatorTestHarness;
import org.apache.flink.util.Collector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.test.appender.ListAppender;
import org.apache.logging.log4j.core.test.junit.LoggerContextSource;
import org.apache.logging.log4j.core.test.junit.Named;
import org.apache.logging.log4j.layout.template.json.util.JsonReader;
import org.junit.jupiter.api.Test;

public class LoggingAspectTest {
    private final Logger logger = LogManager.getLogger(LoggingAspectTest.class);

    @Test
    @LoggerContextSource("log4j2-listappender.properties")
    void test(final @Named(value = "ListAppender") ListAppender appender) throws Exception {
        try (OneInputStreamOperatorTestHarness<Integer, Integer> harness =
                new OneInputStreamOperatorTestHarness<>(
                        new StreamMap<>(new NonLeapRecordMapFunction()))) {
            harness.open();
            harness.processElement(1, System.currentTimeMillis());
            Optional<StreamRecord<Integer>> output = harness.getRecordOutput().stream().findFirst();
            assertFalse(output.isEmpty(), "Element is outputted");
            assertEquals(2, output.get().getValue());
        }

        final LinkedHashMap<String, String> actualLoggedEvent = getFirstLoggedEvent(appender);

        assertNotNull(actualLoggedEvent, "Log should exists.");
        assertNull(actualLoggedEvent.get(LeapRecordConstants.URC), "URC should not be injected.");
    }

    @Test
    @LoggerContextSource("log4j2-listappender.properties")
    void testWithConcreteMapFunction(final @Named(value = "ListAppender") ListAppender appender)
            throws Exception {
        testMapFunction(new LeapRecordMapFunction(), appender);
    }

    @Test
    @LoggerContextSource("log4j2-listappender.properties")
    void testWithLambdaMapFunction(final @Named(value = "ListAppender") ListAppender appender)
            throws Exception {
        testMapFunction(
                value -> {
                    logger.info("Value is " + value.getValue().getData());
                    return value;
                },
                appender);
    }

    @Test
    @LoggerContextSource("log4j2-listappender.properties")
    void testWithAnonymousMapFunction(final @Named(value = "ListAppender") ListAppender appender)
            throws Exception {
        testMapFunction(
                new MapFunction<LeapEventIntRecord, LeapEventIntRecord>() {
                    @Override
                    public LeapEventIntRecord map(LeapEventIntRecord value) throws Exception {
                        logger.info("Value is " + value.getValue().getData());
                        return value;
                    }
                },
                appender);
    }

    @Test
    @LoggerContextSource("log4j2-listappender.properties")
    void testWithConcreteFilterFunction(final @Named(value = "ListAppender") ListAppender appender)
            throws Exception {
        testFilterFunction(new LeapRecordFilterFunction(), appender);
    }

    @Test
    @LoggerContextSource("log4j2-listappender.properties")
    void testWithLambdaFilterFunction(final @Named(value = "ListAppender") ListAppender appender)
            throws Exception {
        testFilterFunction(
                (FilterFunction<LeapEventIntRecord>)
                        value -> {
                            logger.info("Value is " + value.getValue().getData());
                            return true;
                        },
                appender);
    }

    @Test
    @LoggerContextSource("log4j2-listappender.properties")
    void testWithAnonymousFilterFunction(final @Named(value = "ListAppender") ListAppender appender)
            throws Exception {
        testFilterFunction(
                new FilterFunction<LeapEventIntRecord>() {
                    @Override
                    public boolean filter(LeapEventIntRecord value) throws Exception {
                        logger.info("Value is " + value.getValue().getData());
                        return true;
                    }
                },
                appender);
    }

    @Test
    @LoggerContextSource("log4j2-listappender.properties")
    void testWithConcreteFlatMapFunction(final @Named(value = "ListAppender") ListAppender appender)
            throws Exception {
        testFlatMapFunction(new LeapRecordFlatMapFunction(), appender);
    }

    @Test
    @LoggerContextSource("log4j2-listappender.properties")
    void testWithLambdaFlatMapFunction(final @Named(value = "ListAppender") ListAppender appender)
            throws Exception {
        testFlatMapFunction(
                (value, collector) -> {
                    logger.info("Value is " + value.getValue().getData());
                    collector.collect(value);
                },
                appender);
    }

    @Test
    @LoggerContextSource("log4j2-listappender.properties")
    void testWithAnonymousFlatMapFunction(
            final @Named(value = "ListAppender") ListAppender appender) throws Exception {
        testFlatMapFunction(
                new FlatMapFunction<LeapEventIntRecord, LeapEventIntRecord>() {
                    @Override
                    public void flatMap(
                            LeapEventIntRecord value, Collector<LeapEventIntRecord> collector)
                            throws Exception {
                        logger.info("Value is " + value.getValue().getData());
                        collector.collect(value);
                    }
                },
                appender);
    }

    @Test
    @LoggerContextSource("log4j2-listappender.properties")
    public <T> void testWithConcreteKeyedCoProcessFunction(
            final @Named(value = "ListAppender") ListAppender appender) throws Exception {
        testKeyedCoProcessFunction(new LeapRecordKeyedCoProcessFunction(), appender);
    }

    private void testMapFunction(
            MapFunction<LeapEventIntRecord, LeapEventIntRecord> function, ListAppender appender)
            throws Exception {
        testOperator(
                new StreamMap<>(function),
                LeapEventIntRecord.getTestRecord(1, 1, "1", "map"),
                appender);
    }

    private void testFilterFunction(
            FilterFunction<LeapEventIntRecord> function, ListAppender appender) throws Exception {
        testOperator(
                new StreamFilter<>(function),
                LeapEventIntRecord.getTestRecord(1, 1, "1", "filter"),
                appender);
    }

    private void testFlatMapFunction(
            FlatMapFunction<LeapEventIntRecord, LeapEventIntRecord> function, ListAppender appender)
            throws Exception {
        testOperator(
                new StreamFlatMap<>(function),
                LeapEventIntRecord.getTestRecord(1, 1, "1", "map"),
                appender);
    }

    private void testKeyedCoProcessFunction(
            KeyedCoProcessFunction<
                            String, LeapEventIntRecord, LeapEventIntRecord, LeapEventIntRecord>
                    function,
            ListAppender appender)
            throws Exception {
        testKeyedTwoInputOperator(
                new KeyedCoProcessOperator<>(function),
                LeapEventIntRecord.getTestRecord(1, 1, "1", "map"),
                LeapEventIntRecord.getTestRecord(1, 1, "2", "map"),
                appender);
    }

    private <T> void testOperator(
            OneInputStreamOperator<T, T> operator, T testRecord, ListAppender appender)
            throws Exception {

        try (OneInputStreamOperatorTestHarness<T, T> harness =
                new OneInputStreamOperatorTestHarness<>(operator)) {
            harness.open();
            harness.processElement(testRecord, System.currentTimeMillis());
        }

        final LinkedHashMap<String, String> actualLoggedEvent = getFirstLoggedEvent(appender);

        assertNotNull(actualLoggedEvent, "Log should exist.");
        assertEquals(
                "1",
                actualLoggedEvent.get(LeapRecordConstants.URC),
                "URC should be injected correctly");
    }

    private <T> void testKeyedTwoInputOperator(
            TwoInputStreamOperator<T, T, T> operator,
            T firstRecord,
            T secondRecord,
            ListAppender appender)
            throws Exception {
        try (KeyedTwoInputStreamOperatorTestHarness<String, T, T, T> harness =
                new KeyedTwoInputStreamOperatorTestHarness<>(
                        operator, v -> "1", v -> "1", TypeInformation.of(String.class))) {
            harness.setup();
            harness.open();
            harness.processElement1(firstRecord, System.currentTimeMillis());
            harness.processElement2(secondRecord, System.currentTimeMillis());
        }

        LinkedHashMap<String, String>[] loggedEvents = getLoggedEvents(appender);

        assertEquals(2, loggedEvents.length, "Two logs should exist.");
        assertNotNull(loggedEvents[0], "First Log should exist.");
        assertEquals(
                "1",
                loggedEvents[0].get(LeapRecordConstants.URC),
                "URC should be injected correctly");

        assertNotNull(loggedEvents[1], "Second Log should exist.");
        assertEquals(
                "2",
                loggedEvents[1].get(LeapRecordConstants.URC),
                "URC should be injected correctly");
    }

    private <T> void testTwoInputOperator(
            TwoInputStreamOperator<T, T, T> operator,
            T firstRecord,
            T secondRecord,
            ListAppender appender)
            throws Exception {
        try (TwoInputStreamOperatorTestHarness<T, T, T> harness =
                new TwoInputStreamOperatorTestHarness<>(operator)) {
            harness.setup();
            harness.open();
            harness.processElement1(firstRecord, System.currentTimeMillis());
            harness.processElement2(secondRecord, System.currentTimeMillis());
        }

        LinkedHashMap<String, String>[] loggedEvents = getLoggedEvents(appender);

        assertEquals(2, loggedEvents.length, "Two logs should exist.");
        assertNotNull(loggedEvents[0], "First Log should exist.");
        assertEquals(
                "1",
                loggedEvents[0].get(LeapRecordConstants.URC),
                "URC should be injected correctly");

        assertNotNull(loggedEvents[1], "Second Log should exist.");
        assertEquals(
                "2",
                loggedEvents[1].get(LeapRecordConstants.URC),
                "URC should be injected correctly");
    }

    private LinkedHashMap<String, String> getFirstLoggedEvent(ListAppender appender) {
        @SuppressWarnings("unchecked")
        final LinkedHashMap<String, String> loggedEvent =
                appender.getData().stream()
                        .map(
                                jsonBytes -> {
                                    final String json =
                                            new String(jsonBytes, StandardCharsets.UTF_8);
                                    return (LinkedHashMap<String, String>) JsonReader.read(json);
                                })
                        .findFirst()
                        .orElse(null);
        return loggedEvent;
    }

    private LinkedHashMap<String, String>[] getLoggedEvents(ListAppender appender) {
        @SuppressWarnings("unchecked")
        final LinkedHashMap<String, String>[] loggedEvents =
                appender.getData().stream()
                        .map(
                                jsonBytes -> {
                                    final String json =
                                            new String(jsonBytes, StandardCharsets.UTF_8);
                                    return (LinkedHashMap<String, String>) JsonReader.read(json);
                                })
                        .toArray(LinkedHashMap[]::new);
        return loggedEvents;
    }
}
