package com.gimral.streaming.core.job;

import com.gimral.streaming.core.LeapFlinkJob;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class DummyLeapFlinkJob extends LeapFlinkJob {
    @Override
    public void build(StreamExecutionEnvironment env) {
        // No-op for test
    }
}
