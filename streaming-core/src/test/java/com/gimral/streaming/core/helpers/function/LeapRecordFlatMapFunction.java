package com.gimral.streaming.core.helpers.function;

import com.gimral.streaming.core.model.LeapRecord;
import com.gimral.streaming.core.model.LogLeapEvent;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeapRecordFlatMapFunction implements FlatMapFunction<LeapRecord<LogLeapEvent>,LeapRecord<LogLeapEvent>> {
    private final Logger logger = LogManager.getLogger(LeapRecordFlatMapFunction.class);

    @Override
    public void flatMap(LeapRecord<LogLeapEvent> value, Collector<LeapRecord<LogLeapEvent>> out) throws Exception {
        // Custom mapping logic
        logger.info("Value is " + value.getValue().getData());
        out.collect(value);
    }
}
