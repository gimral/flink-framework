package com.gimral.streaming.core.aop;

import com.gimral.streaming.core.helpers.function.LeapRecordFlatMapFunction;
import com.gimral.streaming.core.helpers.function.LeapRecordMapFunction;
import com.gimral.streaming.core.helpers.function.LeapRecordRichMapFunction;
import com.gimral.streaming.core.helpers.function.NonLeapRecordMapFunction;
import com.gimral.streaming.core.model.LeapRecord;
import com.gimral.streaming.core.model.LogLeapEvent;
import org.apache.flink.runtime.operators.testutils.DiscardingOutputCollector;
import org.apache.logging.log4j.core.test.appender.ListAppender;
import org.apache.logging.log4j.core.test.junit.LoggerContextSource;
import org.apache.logging.log4j.core.test.junit.Named;
import org.apache.logging.log4j.layout.template.json.util.JsonReader;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class LoggingAspectTest {

    @Test
    @LoggerContextSource("log4j2-listappender.properties")
    public void testMapFunction(final @Named(value = "ListAppender") ListAppender appender) {
        LeapRecordMapFunction mapper = new LeapRecordMapFunction();
        LeapRecord<LogLeapEvent> record = LogLeapEvent.getTestRecord(1,1,"1","map");
        mapper.map(record);

        final LinkedHashMap<String, String> actualLoggedEvent = getFirstLoggedEvent(appender);

        assertNotNull(actualLoggedEvent);
        assertEquals("1",actualLoggedEvent.get("urc"));
    }

    @Test
    @LoggerContextSource("log4j2-listappender.properties")
    public void testMapFunctionWithoutLeapRecordArg(final @Named(value = "ListAppender") ListAppender appender) throws Exception {
        NonLeapRecordMapFunction mapper = new NonLeapRecordMapFunction();
        mapper.map("1");

        final LinkedHashMap<String, String> actualLoggedEvent = getFirstLoggedEvent(appender);

        assertNotNull(actualLoggedEvent);
        assertNull(actualLoggedEvent.get("urc"));
    }
    @Test
    @LoggerContextSource("log4j2-listappender.properties")
    public void testRichMapFunction(final @Named(value = "ListAppender") ListAppender appender) {
        LeapRecordRichMapFunction mapper = new LeapRecordRichMapFunction();
        LeapRecord<LogLeapEvent> record = LogLeapEvent.getTestRecord(1,1,"1","map");

        mapper.map(record);

        final LinkedHashMap<String, String> actualLoggedEvent = getFirstLoggedEvent(appender);

        assertNotNull(actualLoggedEvent);
        assertEquals("1",actualLoggedEvent.get("urc"));
    }

    @Test
    @LoggerContextSource("log4j2-listappender.properties")
    public void testFlatMapFunction(final @Named(value = "ListAppender") ListAppender appender) throws Exception {
        LeapRecordFlatMapFunction mapper = new LeapRecordFlatMapFunction();
        LeapRecord<LogLeapEvent> record = LogLeapEvent.getTestRecord(1,1,"1","map");

        mapper.flatMap(record,new DiscardingOutputCollector<>());

        final LinkedHashMap<String, String> actualLoggedEvent = getFirstLoggedEvent(appender);

        assertNotNull(actualLoggedEvent);
        assertEquals("1",actualLoggedEvent.get("urc"));
    }

    private LinkedHashMap<String, String> getFirstLoggedEvent(ListAppender appender){
        @SuppressWarnings("unchecked")
        final LinkedHashMap<String, String> loggedEvent =
                appender.getData().stream()
                        .map(
                                jsonBytes -> {
                                    final String json = new String(jsonBytes, StandardCharsets.UTF_8);
                                    return (LinkedHashMap<String, String>) JsonReader.read(json);
                                })
                        .findFirst()
                        .orElse(null);
        return loggedEvent;
    }


}
