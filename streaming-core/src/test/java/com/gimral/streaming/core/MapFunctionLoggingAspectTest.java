package com.gimral.streaming.core;

import com.gimral.streaming.core.function.FlinkMapFunction;
import com.gimral.streaming.core.function.FlinkRichMapFunction;
import com.gimral.streaming.core.model.LeapRecord;
import org.apache.logging.log4j.core.test.appender.ListAppender;
import org.apache.logging.log4j.core.test.junit.LoggerContextSource;
import org.apache.logging.log4j.core.test.junit.Named;
import org.apache.logging.log4j.layout.template.json.util.JsonReader;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MapFunctionLoggingAspectTest {

    @Test
    @LoggerContextSource("log4j2-listappender.properties")
    public void testMappingJob(final @Named(value = "ListAppender") ListAppender appender) {
        FlinkMapFunction mapper = new FlinkMapFunction();
        LeapRecord<Integer> record = new LeapRecord<>(1,1,"1","map");

        mapper.map(record);

        final LinkedHashMap<String, String> actualLoggedEvent = getFirstLoggedEvent(appender);

        assertNotNull(actualLoggedEvent);
        assertEquals("1",actualLoggedEvent.get("urc"));
    }

    @Test
    @LoggerContextSource("log4j2-listappender.properties")
    public void testRichMappingJob(final @Named(value = "ListAppender") ListAppender appender) {
        FlinkRichMapFunction mapper = new FlinkRichMapFunction();
        LeapRecord<Integer> record = new LeapRecord<>(1,1,"1","map");

        mapper.map(record);

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
