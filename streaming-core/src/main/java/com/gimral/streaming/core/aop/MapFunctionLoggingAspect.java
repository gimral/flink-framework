
package com.gimral.streaming.core.aop;

import com.gimral.streaming.core.model.LeapEvent;
import com.gimral.streaming.core.model.LeapRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;

import java.io.Closeable;
import java.util.Arrays;

/**
 * AspectJ interceptor for all Flink MapFunction and FlatMapFunction
 * implementations.
 * Injects LeapRecord and LeapEvent fields into the MDC for logging context.
 */
@Aspect
public class MapFunctionLoggingAspect {

    /**
     * Intercepts all map methods of MapFunction and flatMap methods of
     * FlatMapFunction.
     * Injects relevant fields into MDC for logging, and ensures cleanup after
     * execution.
     */
    @Around("(execution(* org.apache.flink.api.common.functions.MapFunction+.map(..)) && args(record)) || " +
            "(execution(* org.apache.flink.api.common.functions.FlatMapFunction+.flatMap(..)) && args(record, *))")
    public Object log(ProceedingJoinPoint joinPoint, LeapRecord<?> record) throws Throwable {
        try (MDCInjector ignored = MDCInjector.putAll(record)) {
            return joinPoint.proceed();
        }
    }

    /**
     * Utility for safely injecting and removing MDC context for LeapRecord fields.
     */
    private static class MDCInjector implements Closeable {
        private static final String MDC_URC = "urc";
        private static final String MDC_TIMESTAMP = "timestamp";
        private static final String MDC_TYPE = "type";
        private static final String MDC_METADATA = "metadata";

        public static MDCInjector putAll(LeapRecord<?> record) {
            if (record != null) {
                LeapEvent<?> event = record.getValue();
                if (event != null) {
                    putIfNotNull(MDC_URC, event.getUrc());
                    putIfNotNull(MDC_TIMESTAMP, event.getTimestamp() != null ? event.getTimestamp().toString() : null);
                    putIfNotNull(MDC_TYPE, event.getType());
                }
                putIfNotNull(MDC_METADATA, record.getMetadata() != null ? Arrays.toString(record.getMetadata()) : null);
            }
            return new MDCInjector();
        }

        private static void putIfNotNull(String key, String value) {
            if (value != null) {
                MDC.put(key, value);
            }
        }

        @Override
        public void close() {
            MDC.remove(MDC_URC);
            MDC.remove(MDC_TIMESTAMP);
            MDC.remove(MDC_TYPE);
            MDC.remove(MDC_METADATA);
        }
    }
}
