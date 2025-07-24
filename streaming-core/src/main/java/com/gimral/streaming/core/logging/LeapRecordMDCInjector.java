package com.gimral.streaming.core.logging;

import com.gimral.streaming.core.model.LeapEvent;
import com.gimral.streaming.core.model.LeapRecord;
import org.slf4j.MDC;

import java.io.Closeable;
import java.util.Arrays;

/**
 * Utility for safely injecting and removing MDC context for LeapRecord fields.
 */
public class LeapRecordMDCInjector implements Closeable {
    private static final String MDC_URC = "urc";
    private static final String MDC_TIMESTAMP = "timestamp";
    private static final String MDC_TYPE = "type";
    private static final String MDC_METADATA = "metadata";

    public static LeapRecordMDCInjector putAll(LeapRecord<?> record) {
        if(record == null)
            return new LeapRecordMDCInjector();

        putIfNotNull(MDC_METADATA, record.getMetadata() != null ? Arrays.toString(record.getMetadata()) : null);

        if (!(record.getValue() instanceof LeapEvent<?> event))
            return new LeapRecordMDCInjector();

        putIfNotNull(MDC_URC, event.getUrc());
        putIfNotNull(MDC_TIMESTAMP, event.getTimestamp() != null ? event.getTimestamp().toString() : null);
        putIfNotNull(MDC_TYPE, event.getType());
        return new LeapRecordMDCInjector();
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