package com.gimral.streaming.core.functions;

import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

/**
 * Delegate wrapper for Flink KeyedCoProcessFunction to intercept or extend
 * behavior.
 */
public abstract class LeapKeyedProcessFunction<K, I, O> extends KeyedProcessFunction<K, I, O> {

    @Override
    public void open(OpenContext ctx) throws Exception {
        // Subclasses can intercept or extend this behavior
        innerOpen(ctx);
    }

    public abstract void innerOpen(OpenContext ctx) throws Exception;

    @Override
    public void processElement(I value, Context ctx, Collector<O> out) throws Exception {
        // Subclasses can intercept or extend this behavior
        innerProcessElement(value, ctx, out);
    }

    public abstract void innerProcessElement(I value, Context ctx, Collector<O> out) throws Exception;
}
