package com.gimral.streaming.runtime.model;

import com.gimral.streaming.core.model.LeapEvent;
import com.gimral.streaming.core.model.LeapRecord;

public class LeapEventIntRecord extends LeapRecord<LeapEvent<Integer>> {
    public static LeapEventIntRecord getTestRecord(
            Integer data, long timestamp, String urc, String type) {
        LeapEventIntRecord record = new LeapEventIntRecord();
        LeapEvent<Integer> value = new LeapEvent<Integer>();
        value.setData(data);
        value.setUrc(urc);
        value.setType(type);
        value.setTimestamp(timestamp);
        record.setValue(value);
        return record;
    }
}
