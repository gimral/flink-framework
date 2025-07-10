package com.gimral.streaming.core.model;

public class LeapRecord<V> {
    private LeapMetaData[] metadata;
    private Object key;
    private LeapEvent<V> value;
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

    public LeapEvent<V> getValue() {
        return value;
    }

    public void setValue(LeapEvent<V> value) {
        this.value = value;
    }

}
