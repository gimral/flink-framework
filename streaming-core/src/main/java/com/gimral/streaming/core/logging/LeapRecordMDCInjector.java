package com.gimral.streaming.core.logging;

import com.gimral.streaming.core.model.LeapEvent;
import com.gimral.streaming.core.model.LeapRecord;
import com.gimral.streaming.core.model.LeapRecordConstants;
import java.io.Closeable;
import java.util.Arrays;
import org.slf4j.MDC;

/** Utility for safely injecting and removing MDC context for LeapRecord fields. */
public class LeapRecordMDCInjector implements Closeable {
    public static LeapRecordMDCInjector putAll(LeapRecord<?> record) {
        if (record == null) return new LeapRecordMDCInjector();

        putIfNotNull(
                LeapRecordConstants.METADATA,
                record.getMetadata() != null ? Arrays.toString(record.getMetadata()) : null);

        if (!(record.getValue() instanceof LeapEvent<?> event)) return new LeapRecordMDCInjector();

        putIfNotNull(LeapRecordConstants.EVENT_ID, event.getEventId());
        putIfNotNull(LeapRecordConstants.URC, event.getUrc());
        putIfNotNull(LeapRecordConstants.GRC, event.getGrc());
        putIfNotNull(LeapRecordConstants.CHANNEL_ID, event.getChannelId());
        putIfNotNull(LeapRecordConstants.FINANCIAL_ID, event.getFinancialId());
        putIfNotNull(
                LeapRecordConstants.TIMESTAMP,
                event.getTimestamp() != null ? event.getTimestamp().toString() : null);
        putIfNotNull(LeapRecordConstants.TYPE, event.getType());

        return new LeapRecordMDCInjector();
    }

    private static void putIfNotNull(String key, String value) {
        if (value != null) {
            MDC.put(key, value);
        }
    }

    @Override
    public void close() {
        MDC.remove(LeapRecordConstants.EVENT_ID);
        MDC.remove(LeapRecordConstants.CHANNEL_ID);
        MDC.remove(LeapRecordConstants.FINANCIAL_ID);
        MDC.remove(LeapRecordConstants.URC);
        MDC.remove(LeapRecordConstants.GRC);
        MDC.remove(LeapRecordConstants.TIMESTAMP);
        MDC.remove(LeapRecordConstants.TYPE);
        MDC.remove(LeapRecordConstants.METADATA);
    }
}
