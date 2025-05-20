package com.gimral.streaming.core;

import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
import org.apache.flink.util.Collector;

/**
 * Delegate wrapper for Flink KeyedCoProcessFunction to intercept or extend
 * behavior.
 */
class LeapKeyedCoProcessFunction<K, IN1, IN2, OUT> extends KeyedCoProcessFunction<K, IN1, IN2, OUT> {
    protected final KeyedCoProcessFunction<K, IN1, IN2, OUT> delegate;

    public LeapKeyedCoProcessFunction(KeyedCoProcessFunction<K, IN1, IN2, OUT> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void open(OpenContext ctx) throws Exception {
        // Subclasses can intercept or extend this behavior
        delegate.open(ctx);
    }

    @Override
    public void processElement1(IN1 value, Context ctx, Collector<OUT> out) throws Exception {
        // Subclasses can intercept or extend this behavior
        delegate.processElement1(value, ctx, out);
    }

    @Override
    public void processElement2(IN2 value, Context ctx, Collector<OUT> out) throws Exception {
        // Subclasses can intercept or extend this behavior
        delegate.processElement2(value, ctx, out);
    }
}
