package com.gimral.streaming.core.helpers.function;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NonLeapRecordMapFunction implements MapFunction<Integer, Integer> {
    private final Logger logger = LogManager.getLogger(LeapRecordMapFunction.class);

    @Override
    public Integer map(Integer value) {
        // Custom mapping logic
        logger.info("Value is " + value);
        return value * 2;
    }
}
