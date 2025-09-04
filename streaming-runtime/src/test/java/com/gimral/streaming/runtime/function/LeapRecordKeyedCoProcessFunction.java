package com.gimral.streaming.runtime.function;

import com.gimral.streaming.runtime.model.LeapEventIntRecord;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeapRecordKeyedCoProcessFunction
        extends KeyedCoProcessFunction<
                String, LeapEventIntRecord, LeapEventIntRecord, LeapEventIntRecord> {
    private final Logger logger = LogManager.getLogger(LeapRecordKeyedCoProcessFunction.class);

    @Override
    public void processElement1(
            LeapEventIntRecord value,
            KeyedCoProcessFunction<
                                    String,
                                    LeapEventIntRecord,
                                    LeapEventIntRecord,
                                    LeapEventIntRecord>
                            .Context
                    ctx,
            Collector<LeapEventIntRecord> out)
            throws Exception {
        logger.info("Value is " + value.getValue().getData());
        out.collect(value);
    }

    @Override
    public void processElement2(
            LeapEventIntRecord value,
            KeyedCoProcessFunction<
                                    String,
                                    LeapEventIntRecord,
                                    LeapEventIntRecord,
                                    LeapEventIntRecord>
                            .Context
                    ctx,
            Collector<LeapEventIntRecord> out)
            throws Exception {
        logger.info("Value is " + value.getValue().getData());
        out.collect(value);
    }
}
