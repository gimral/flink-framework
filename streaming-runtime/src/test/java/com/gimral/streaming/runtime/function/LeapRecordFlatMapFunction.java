package com.gimral.streaming.runtime.function;

import com.gimral.streaming.runtime.model.LeapEventIntRecord;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeapRecordFlatMapFunction
        implements FlatMapFunction<LeapEventIntRecord, LeapEventIntRecord> {
    private final Logger logger = LogManager.getLogger(LeapRecordFlatMapFunction.class);

    @Override
    public void flatMap(LeapEventIntRecord value, Collector<LeapEventIntRecord> out)
            throws Exception {
        logger.info("Value is " + value.getValue().getData());
        out.collect(value);
    }
}
