package com.gimral.streaming.core.functions;

import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
import org.apache.flink.util.Collector;

/**
 * Delegate wrapper for Flink KeyedCoProcessFunction to intercept or extend
 * behavior.
 */
public abstract class LeapKeyedCoProcessFunction<K, IN1, IN2, OUT> extends KeyedCoProcessFunction<K, IN1, IN2, OUT> {

    @Override
    public void open(OpenContext ctx) throws Exception {
        // Subclasses can intercept or extend this behavior
        innerOpen(ctx);
    }

    public abstract void innerOpen(OpenContext ctx) throws Exception;

    @Override
    public void processElement1(IN1 value, Context ctx, Collector<OUT> out) throws Exception {
        // Subclasses can intercept or extend this behavior
        innerProcessElement1(value, ctx, out);
    }

    public abstract void innerProcessElement1(IN1 value, Context ctx, Collector<OUT> out) throws Exception;

    @Override
    public void processElement2(IN2 value, Context ctx, Collector<OUT> out) throws Exception {
        // Subclasses can intercept or extend this behavior
        innerProcessElement2(value, ctx, out);
    }

    public abstract void innerProcessElement2(IN2 value, Context ctx, Collector<OUT> out) throws Exception;

}
