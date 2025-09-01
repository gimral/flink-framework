package com.gimral.streaming.core.model;

// @TypeInfo(LeapRecordTypeInfoFactory.class)
public class LeapRecord<V> {
    private LeapMetaData[] metadata;
    private String key;
    private V value;

    public LeapMetaData[] getMetadata() {
        return metadata;
    }

    public void setMetadata(LeapMetaData[] metadata) {
        this.metadata = metadata;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
