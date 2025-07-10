package com.gimral.streaming.core.function;

import com.gimral.streaming.core.model.LeapRecord;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FlinkRichMapFunction extends RichMapFunction<LeapRecord<Integer>,LeapRecord<Integer>> {
    private final Logger logger = LogManager.getLogger(FlinkRichMapFunction.class);
    @Override
    public LeapRecord<Integer> map(LeapRecord<Integer> value) {
        // Custom mapping logic
        logger.info("Value is " + value.getValue().getData());
        return value;
    }
}
