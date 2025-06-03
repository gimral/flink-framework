package com.gimral.streaming.core.datastream;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.KeyedStream.IntervalJoin;
import org.apache.flink.streaming.api.functions.sink.legacy.SinkFunction;

import com.gimral.streaming.core.functions.LeapKeyedProcessFunction;

public class LeapKeyedStream<T, KEY> extends LeapDataStream<T> {
    private final KeyedStream<T, KEY> delegate;

    public LeapKeyedStream(KeyedStream<T, KEY> delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    public KeyedStream<T, KEY> getDelegate() {
        return delegate;
    }

    @Override
    public <R> LeapSingleOutputStreamOperator<R> flatMap(FlatMapFunction<T, R> flatMapper) {
        return new LeapSingleOutputStreamOperator<>(delegate.flatMap(flatMapper));
    }

    public DataStreamSink<T> addSink(SinkFunction<T> sinkFunction) {
        DataStreamSink<T> result = delegate.addSink(sinkFunction);
        return result;
    }

    public <R> LeapSingleOutputStreamOperator<R> process(
            LeapKeyedProcessFunction<KEY, T, R> keyedProcessFunction) {
        return new LeapSingleOutputStreamOperator<>(delegate.process(keyedProcessFunction));
    }

    public <T1> IntervalJoin<T, T1, KEY> intervalJoin(LeapKeyedStream<T1, KEY> otherStream) {
        return delegate.intervalJoin(otherStream.getDelegate());
    }

}
