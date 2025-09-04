package com.gimral.streaming.runtime.function;

import com.gimral.streaming.runtime.model.LeapEventIntRecord;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeapRecordMapFunction implements MapFunction<LeapEventIntRecord, LeapEventIntRecord> {
    private final Logger logger = LogManager.getLogger(LeapRecordMapFunction.class);

    @Override
    public LeapEventIntRecord map(LeapEventIntRecord value) {
        logger.info("Value is " + value.getValue().getData());
        return value;
    }
}
