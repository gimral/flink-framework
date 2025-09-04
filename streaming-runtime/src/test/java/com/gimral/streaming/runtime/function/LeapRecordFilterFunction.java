package com.gimral.streaming.runtime.function;

import com.gimral.streaming.runtime.model.LeapEventIntRecord;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeapRecordFilterFunction implements FilterFunction<LeapEventIntRecord> {
    private final Logger logger = LogManager.getLogger(LeapRecordFilterFunction.class);

    @Override
    public boolean filter(LeapEventIntRecord value) {
        logger.info("Value is " + value.getValue().getData());
        return false;
    }
}
