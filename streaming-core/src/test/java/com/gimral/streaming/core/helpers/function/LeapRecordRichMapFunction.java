package com.gimral.streaming.core.helpers.function;

import com.gimral.streaming.core.model.LeapRecord;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeapRecordRichMapFunction extends RichMapFunction<LeapRecord<Integer>,LeapRecord<Integer>> {
    private final Logger logger = LogManager.getLogger(LeapRecordRichMapFunction.class);
    @Override
    public LeapRecord<Integer> map(LeapRecord<Integer> value) {
        // Custom mapping logic
        logger.info("Value is " + value.getValue().getData());
        return value;
    }
}
