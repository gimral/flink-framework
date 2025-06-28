package com.gimral.streaming.logger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.test.appender.ListAppender;
import org.apache.logging.log4j.core.test.junit.LoggerContextSource;
import org.apache.logging.log4j.core.test.junit.Named;
import org.apache.logging.log4j.layout.template.json.util.JsonReader;
import org.junit.jupiter.api.Test;

public class MaskingResolverTest {
  @Test
  @LoggerContextSource("log4j2.properties")
  void testMaskPattern(final @Named(value = "List") ListAppender appender) {
    final String logMessage = "My card is 1234567890123456 and should be masked.";
    LogManager.getLogger(MaskingResolverTest.class).info(logMessage);

    // Collect and parse logged messages.
    final Object[] actualLoggedEvents =
        appender.getData().stream()
            .map(
                jsonBytes -> {
                  final String json = new String(jsonBytes, StandardCharsets.UTF_8);
                  return JsonReader.read(json);
                })
            .toList()
            .toArray();

    // Verify logged messages.
    final Object[] expectedLoggedEvents =
        Stream.of("My card is 1234******5678 and should be masked.")
            .map(message -> Collections.singletonMap("message", message))
            .toArray();

    assertArrayEquals(expectedLoggedEvents, actualLoggedEvents);
  }
}
