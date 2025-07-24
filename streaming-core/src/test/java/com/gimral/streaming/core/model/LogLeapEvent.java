package com.gimral.streaming.core.model;

public class LogLeapEvent extends LeapEvent<Integer> {

    private String extra;

    public LogLeapEvent() {
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public static LeapRecord<LogLeapEvent> getTestRecord(Integer data, long timestamp, String urc, String type){
        LeapRecord<LogLeapEvent> record = new LeapRecord<>();
        LogLeapEvent value = new LogLeapEvent();
        value.setData(data);
        value.setUrc(urc);
        value.setType(type);
        value.setTimestamp(timestamp);
        record.setValue(value);
        return record;
    }
}
