package com.gimral.streaming.core.function;

import com.gimral.streaming.core.model.LeapRecord;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NonLeapRecordMapFunction implements MapFunction<String,String> {
    private final Logger logger = LogManager.getLogger(LeapRecordMapFunction.class);
    @Override
    public String map(String value) {
        // Custom mapping logic
        logger.info("Value is " + value);
        return value;
    }
}
