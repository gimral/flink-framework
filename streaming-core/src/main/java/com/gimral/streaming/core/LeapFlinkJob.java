package com.gimral.streaming.core;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/** Abstract base class for Flink streaming jobs in the framework. */
public abstract class LeapFlinkJob {
    /**
     * Entry point for job execution. Subclasses should implement this method to define the job
     * pipeline.
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
        // TODO: Ad job name to the MDC and tracing context
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        build(env);
        env.execute(jobName);
    }
}
