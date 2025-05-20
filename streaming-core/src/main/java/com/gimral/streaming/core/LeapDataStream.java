package com.gimral.streaming.core;

import org.apache.flink.api.common.functions.*;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.api.connector.sink2.Sink;

/**
 * Wrapper for Flink DataStream to intercept all operations performed on it.
 */
public class LeapDataStream<T> {
    private final DataStream<T> delegate;

    public LeapDataStream(DataStream<T> delegate) {
        this.delegate = delegate;
    }

    public <R> LeapDataStream<R> map(MapFunction<T, R> mapper) {
        System.out.println("Intercepted map operation");
        LeapMapFunction<T, R> leapMapFunction = new LeapMapFunction<>(mapper);
        return new LeapDataStream<>(delegate.map(leapMapFunction));
    }

    public LeapDataStream<T> filter(FilterFunction<T> filter) {
        System.out.println("Intercepted filter operation");
        return new LeapDataStream<>(delegate.filter(filter));
    }

    public <R> LeapDataStream<R> flatMap(FlatMapFunction<T, R> flatMapper) {
        System.out.println("Intercepted flatMap operation");
        return new LeapDataStream<>(delegate.flatMap(flatMapper));
    }

    public <K> KeyedStream<T, K> keyBy(KeySelector<T, K> keySelector) {
        System.out.println("Intercepted keyBy operation");
        return delegate.keyBy(keySelector);
    }

    @SafeVarargs
    public final LeapDataStream<T> union(DataStream<T>... streams) {
        System.out.println("Intercepted union operation");
        return new LeapDataStream<>(delegate.union(streams));
    }

    public <R> LeapDataStream<R> process(ProcessFunction<T, R> function) {
        System.out.println("Intercepted process operation");
        return new LeapDataStream<>(delegate.process(function));s
    }

    public void sinkTo(Sink<T> sink) {
        System.out.println("Intercepted sinkTo operation");
        delegate.sinkTo(sink);
    }

    public <W extends Window> AllWindowedStream<T, W> windowAll(WindowAssigner<? super T, W> assigner) {
        System.out.println("Intercepted windowAll operation");
        return delegate.windowAll(assigner);
    }
}
