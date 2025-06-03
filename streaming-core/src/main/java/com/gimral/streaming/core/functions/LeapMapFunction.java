package com.gimral.streaming.core.functions;

import org.apache.flink.api.common.functions.MapFunction;

import com.gimral.streaming.core.model.ErrorInputOutput;

/**
 * Delegate wrapper for Flink MapFunction to intercept or extend behavior.
 */
class LeapMapFunction<IN, OUT> implements MapFunction<IN, ErrorInputOutput<IN, OUT>> {
    protected final MapFunction<IN, OUT> delegate;

    public LeapMapFunction(MapFunction<IN, OUT> delegate) {
        this.delegate = delegate;
    }

    @Override
    public ErrorInputOutput<IN, OUT> map(IN value) throws Exception {
        try {
            return ErrorInputOutput.success(delegate.map(value));
        } catch (Exception e) {
            // TODO:Log the exception as needed
            return ErrorInputOutput.error(value);
        }

    }
}
