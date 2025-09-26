package com.gimral.streaming.runtime.logging;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gimral.streaming.core.model.LeapEvent;
import com.gimral.streaming.core.model.LeapMetaData;
import com.gimral.streaming.core.model.LeapRecord;
import com.gimral.streaming.core.model.LeapRecordConstants;
import java.io.Closeable;
import java.util.Arrays;
import org.slf4j.MDC;

/** Utility for safely injecting and removing MDC context for LeapRecord fields. */
public class LeapRecordMDCInjector implements Closeable {
    public static LeapRecordMDCInjector putAll(LeapRecord<?> record) {
        if (record == null) return new LeapRecordMDCInjector();

        for(int i=0;i< (record.getMetadata()!=null?record.getMetadata().length:0);i++){
            LeapMetaData md = record.getMetadata()[i];
            String prefix = LeapRecordConstants.METADATA + (record.getMetadata().length > 0 ?  "." + i : "");
            if(md!=null) populateMetaData(md,prefix);
        }

        if ((record.getValue() instanceof LeapEvent<?> event)) populateForLeapEvent(event);
        else if (record.getValue() instanceof ObjectNode node) populateForObjectNode(node);

        return new LeapRecordMDCInjector();
    }

    private static void populateMetaData(LeapMetaData md,String prefix){
        putIfNotNull(prefix + "." + LeapRecordConstants.METADATA_SOURCE, md.getSource());
        putIfNotNull(prefix + "." + LeapRecordConstants.METADATA_PARTITION, md.getPartition());
        putIfNotNull(prefix + "." + LeapRecordConstants.METADATA_OFFSET, md.getOffset());
        putIfNotNull(prefix + "." + LeapRecordConstants.METADATA_TIMESTAMP, md.getTimestamp());
    }

    private static void populateForLeapEvent(LeapEvent<?> event) {
        putIfNotNull(LeapRecordConstants.EVENT_ID, event.getEventId());
        putIfNotNull(LeapRecordConstants.URC, event.getUrc());
        putIfNotNull(LeapRecordConstants.GRC, event.getGrc());
        putIfNotNull(LeapRecordConstants.CHANNEL_ID, event.getChannelId());
        putIfNotNull(LeapRecordConstants.FINANCIAL_ID, event.getFinancialId());
        putIfNotNull(
                LeapRecordConstants.TIMESTAMP,
                event.getTimestamp() != null ? event.getTimestamp().toString() : null);
        putIfNotNull(LeapRecordConstants.TYPE, event.getType());
    }

    private static void populateForObjectNode(ObjectNode node) {
        putIfNotNull(
                LeapRecordConstants.EVENT_ID, getObjectNodeField(node, LeapRecordConstants.EVENT_ID));
        putIfNotNull(LeapRecordConstants.URC, getObjectNodeField(node, LeapRecordConstants.URC));
        putIfNotNull(LeapRecordConstants.GRC, getObjectNodeField(node, LeapRecordConstants.GRC));
        putIfNotNull(
                LeapRecordConstants.CHANNEL_ID,
                getObjectNodeField(node, LeapRecordConstants.CHANNEL_ID));
        putIfNotNull(
                LeapRecordConstants.FINANCIAL_ID,
                getObjectNodeField(node, LeapRecordConstants.FINANCIAL_ID));
        putIfNotNull(
                LeapRecordConstants.TIMESTAMP,
                getObjectNodeField(node, LeapRecordConstants.TIMESTAMP));
        putIfNotNull(LeapRecordConstants.TYPE, getObjectNodeField(node, LeapRecordConstants.TYPE));
    }

    private static String getObjectNodeField(ObjectNode node, String fieldName) {
        if (node.has(fieldName) && !node.get(fieldName).isNull()) {
            return node.get(fieldName).asText();
        }
        return null;
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
