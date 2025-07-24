package com.gimral.streaming.core.model;

import org.apache.flink.annotation.Internal;
import org.apache.flink.api.common.typeinfo.TypeInfo;

@Internal
public class LeapRecord<V> {
    private LeapMetaData[] metadata;
    private Object key;
    private V value;
    public LeapMetaData[] getMetadata() {
        return metadata;
    }

    public void setMetadata(LeapMetaData[] metadata) {
        this.metadata = metadata;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

}
