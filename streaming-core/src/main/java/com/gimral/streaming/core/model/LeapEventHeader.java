package com.gimral.streaming.core.model;

public class LeapEventHeader {
    private Long timestamp;
    private String urc;
    private String type;

    public LeapEventHeader(){}

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrc() {
        return urc;
    }

    public void setUrc(String urc) {
        this.urc = urc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
