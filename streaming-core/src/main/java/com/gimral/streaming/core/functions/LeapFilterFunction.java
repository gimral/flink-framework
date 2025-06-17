package com.gimral.streaming.core.functions;

import org.apache.flink.api.common.functions.FilterFunction;

public abstract class LeapFilterFunction<T> implements FilterFunction<T> {
    /**
     * Default implementation that simply returns true, allowing all elements to
     * pass through.
     * Override this method to implement custom filtering logic.
     *
     * @param value the input value to be filtered
     * @return true if the element should be included, false otherwise
     */
    @Override
    public boolean filter(T value) throws Exception {
        return filterCustom(value);
    }

    abstract boolean filterCustom(T value) throws Exception;

}
