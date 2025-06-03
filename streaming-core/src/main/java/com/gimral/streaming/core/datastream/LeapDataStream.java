package com.gimral.streaming.core.datastream;

import org.apache.flink.api.common.functions.*;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.windows.Window;

import com.gimral.streaming.core.functions.ErrorRouterProcess;
import com.gimral.streaming.core.functions.LeapMapFunction;

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

    public <R> LeapSingleOutputStreamOperator<R> map(MapFunction<T, R> mapper) {
        System.out.println("Intercepted map operation");
        LeapMapFunction<T, R> leapMapFunction = new LeapMapFunction<>(mapper);
        return new LeapSingleOutputStreamOperator<>(delegate.map(leapMapFunction)
                .process(new ErrorRouterProcess<>()));
    }

    public LeapSingleOutputStreamOperator<T> filter(FilterFunction<T> filter) {
        System.out.println("Intercepted filter operation");
        return new LeapSingleOutputStreamOperator<>(delegate.filter(filter));
    }

    public <R> LeapSingleOutputStreamOperator<R> flatMap(FlatMapFunction<T, R> flatMapper) {
        System.out.println("Intercepted flatMap operation");
        return new LeapSingleOutputStreamOperator<>(delegate.flatMap(flatMapper));
    }

    public <K> LeapKeyedStream<T, K> keyBy(KeySelector<T, K> keySelector) {
        System.out.println("Intercepted keyBy operation");
        return new LeapKeyedStream<>(delegate.keyBy(keySelector));
    }

    @SafeVarargs
    public final LeapDataStream<T> union(DataStream<T>... streams) {
        System.out.println("Intercepted union operation");
        return new LeapDataStream<>(delegate.union(streams));
    }

    public <R> LeapDataStream<R> process(ProcessFunction<T, R> function) {
        System.out.println("Intercepted process operation");
        return new LeapDataStream<>(delegate.process(function));
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
