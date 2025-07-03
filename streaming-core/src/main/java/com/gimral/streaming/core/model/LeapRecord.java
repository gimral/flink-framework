package com.gimral.streaming.core.model;

public class LeapRecord<T> {
    private final T data;
    private final long timestamp;
    private final String urc;
    private final String type;

    public LeapRecord(T data, long timestamp, String urc, String type) {
        this.data = data;
        this.timestamp = timestamp;
        this.urc = urc;
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getUrc() {
        return urc;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "LeapRecord{" +
                "data=" + data +
                ", timestamp=" + timestamp +
                ", urc='" + urc + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

}
