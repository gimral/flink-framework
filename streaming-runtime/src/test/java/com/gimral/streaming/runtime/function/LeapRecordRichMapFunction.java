package com.gimral.streaming.runtime.function;

import com.gimral.streaming.runtime.model.LeapEventIntRecord;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeapRecordRichMapFunction
        extends RichMapFunction<LeapEventIntRecord, LeapEventIntRecord> {
    private final Logger logger = LogManager.getLogger(LeapRecordRichMapFunction.class);

    @Override
    public LeapEventIntRecord map(LeapEventIntRecord value) {
        logger.info("Value is " + value.getValue().getData());
        return value;
    }
}
