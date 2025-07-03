package com.gimral.streaming.core.function;

import com.gimral.streaming.core.model.LeapRecord;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.util.Collector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FlinkFlatMapFunction implements FlatMapFunction<LeapRecord<Integer>,LeapRecord<Integer>> {
    private final Logger logger = LogManager.getLogger(FlinkFlatMapFunction.class);

    @Override
    public void flatMap(LeapRecord<Integer> value, Collector<LeapRecord<Integer>> out) throws Exception {
        // Custom mapping logic
        logger.info("Value is " + value.getData());
        out.collect(value);
    }
}
