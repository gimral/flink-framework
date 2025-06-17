package com.gimral.streaming.core.functions;

import org.apache.flink.api.common.functions.MapFunction;

import com.gimral.streaming.core.model.ErrorInputOutput;

/**
 * Delegate wrapper for Flink MapFunction to intercept or extend behavior.
 */
@FunctionalInterface
public interface LeapMapFunction<IN, OUT> extends MapFunction<IN, OUT> {
    // protected final MapFunction<IN, OUT> delegate;

    // public LeapMapFunction(MapFunction<IN, OUT> delegate) {
    // this.delegate = delegate;
    // }

    default OUT map(IN value) throws Exception {
        return leapMap(value);
        // try {
        // return ErrorInputOutput.success(delegate.map(value));
        // } catch (Exception e) {
        // // TODO:Log the exception as needed
        // return ErrorInputOutput.error(value);
        // }

    }

    OUT leapMap(IN value);
}
