package com.gimral.streaming.core;

import org.apache.flink.api.common.functions.MapFunction;

/**
 * Delegate wrapper for Flink MapFunction to intercept or extend behavior.
 */
class LeapMapFunction<IN, OUT> implements MapFunction<IN, OUT> {
    protected final MapFunction<IN, OUT> delegate;

    public LeapMapFunction(MapFunction<IN, OUT> delegate) {
        this.delegate = delegate;
    }

    @Override
    public OUT map(IN value) throws Exception {
        // Subclasses can intercept or extend this behavior
        return delegate.map(value);

    }
}
