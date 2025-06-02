package com.gimral.streaming.core;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.operators.SlotSharingGroup;
import org.apache.flink.util.OutputTag;
import org.apache.flink.streaming.api.datastream.SideOutputDataStream;
import org.apache.flink.streaming.api.datastream.CachedDataStream;
import org.apache.flink.streaming.api.datastream.*;

/**
 * Wrapper for Flink DataStream to intercept all operations performed on it.
 */
public class LeapSingleOutputStreamOperator<T> extends LeapDataStream<T> {
    private final SingleOutputStreamOperator<T> delegate;

    public LeapSingleOutputStreamOperator(SingleOutputStreamOperator<T> delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    public SingleOutputStreamOperator<T> getDelegate() {
        return delegate;
    }

    public LeapSingleOutputStreamOperator<T> name(String name) {
        delegate.name(name);
        return this;
    }

    public LeapSingleOutputStreamOperator<T> uid(String uid) {
        delegate.uid(uid);
        return this;
    }

    public LeapSingleOutputStreamOperator<T> setUidHash(String uidHash) {
        delegate.setUidHash(uidHash);
        return this;
    }

    public LeapSingleOutputStreamOperator<T> setParallelism(int parallelism) {
        delegate.setParallelism(parallelism);
        return this;
    }

    public LeapSingleOutputStreamOperator<T> setMaxParallelism(int maxParallelism) {
        delegate.setMaxParallelism(maxParallelism);
        return this;
    }

    public LeapSingleOutputStreamOperator<T> forceNonParallel() {
        delegate.forceNonParallel();
        return this;
    }

    public LeapSingleOutputStreamOperator<T> setBufferTimeout(long timeoutMillis) {
        delegate.setBufferTimeout(timeoutMillis);
        return this;
    }

    public LeapSingleOutputStreamOperator<T> disableChaining() {
        delegate.disableChaining();
        return this;
    }

    public LeapSingleOutputStreamOperator<T> startNewChain() {
        delegate.startNewChain();
        return this;
    }

    public LeapSingleOutputStreamOperator<T> returns(Class<T> typeClass) {
        delegate.returns(typeClass);
        return this;
    }

    public LeapSingleOutputStreamOperator<T> returns(TypeHint<T> typeHint) {
        delegate.returns(typeHint);
        return this;
    }

    public LeapSingleOutputStreamOperator<T> returns(TypeInformation<T> typeInfo) {
        delegate.returns(typeInfo);
        return this;
    }

    public LeapSingleOutputStreamOperator<T> slotSharingGroup(String slotSharingGroup) {
        delegate.slotSharingGroup(slotSharingGroup);
        return this;
    }

    public LeapSingleOutputStreamOperator<T> slotSharingGroup(SlotSharingGroup slotSharingGroup) {
        delegate.slotSharingGroup(slotSharingGroup);
        return this;
    }

    public <X> SideOutputDataStream<X> getSideOutput(OutputTag<X> sideOutputTag) {
        return delegate.getSideOutput(sideOutputTag);
    }

    public LeapSingleOutputStreamOperator<T> setDescription(String description) {
        delegate.setDescription(description);
        return this;
    }

    public CachedDataStream<T> cache() {
        return delegate.cache();
    }

    public LeapSingleOutputStreamOperator<T> enableAsyncState() {
        delegate.enableAsyncState();
        return this;
    }

    public String getName() {
        return delegate.getName();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
