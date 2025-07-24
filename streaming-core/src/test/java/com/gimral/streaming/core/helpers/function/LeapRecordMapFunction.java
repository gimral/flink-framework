package com.gimral.streaming.core.helpers.function;

import com.gimral.streaming.core.model.LeapRecord;
import com.gimral.streaming.core.model.LogLeapEvent;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeapRecordMapFunction implements MapFunction<LeapRecord<LogLeapEvent>,LeapRecord<LogLeapEvent>> {
    private final Logger logger = LogManager.getLogger(LeapRecordMapFunction.class);
    @Override
    public LeapRecord<LogLeapEvent> map(LeapRecord<LogLeapEvent> value) {
        // Custom mapping logic
        logger.info("Value is " + value.getValue().getData());
        return value;
    }
}
