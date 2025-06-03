package com.gimral.streaming.core;

import java.util.ArrayList;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.gimral.streaming.core.datastream.LeapDataStream;

/**
 * Abstract base class for Flink streaming jobs in the framework.
 */
public abstract class FlinkJob {
    /**
     * Entry point for job execution. Subclasses should implement this method to
     * define the job pipeline.
     * 
     * @param env Flink StreamExecutionEnvironment
     * @throws Exception if job execution fails
     */
    public abstract void build(StreamExecutionEnvironment env) throws Exception;

    /**
     * Runs the job using a new StreamExecutionEnvironment.
     * 
     * @param jobName Name of the Flink job
     * @throws Exception if job execution fails
     */
    public final void run(String jobName) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<Long> dataStream = env.fromData(new ArrayList<Long>()).setParallelism(1);
        LeapDataStream<Long> ds = new LeapDataStream<>(dataStream);
        dataStream.keyBy(null).map(null).filter(null);
        ds.keyBy(null).map(null).filter(null);
        ds.filter(s -> s > 0);
        ds = ds.map(new MapFunction<Long, Long>() {
            @Override
            public Long map(Long value) throws Exception {
                // Custom mapping logic
                return value;
            }
        });
        build(env);
        env.execute(jobName);
    }
}
