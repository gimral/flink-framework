package com.gimral.streaming.core.model;

public class LeapEventHeader {
    private Long timestamp;
    private String eventId;
    private String urc;
    private String grc;
    private String financialId;
    private String channelId;
    private String type;

    public LeapEventHeader() {}

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

    public String getGrc() {
        return grc;
    }

    public void setGrc(String grc) {
        this.grc = grc;
    }

    public String getFinancialId() {
        return financialId;
    }

    public void setFinancialId(String financialId) {
        this.financialId = financialId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
