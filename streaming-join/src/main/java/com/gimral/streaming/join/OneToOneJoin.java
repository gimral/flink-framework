package com.gimral.streaming.join;

import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.state.v2.ValueState;
import org.apache.flink.api.common.state.v2.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
import org.apache.flink.util.Collector;

public class OneToOneJoin<K, L, R> extends KeyedCoProcessFunction<K, L, R, Tuple2<K, Tuple2<L, R>>> {
    private static final String LEFT_RECORD = "LeftRecord";
    private static final String RIGHT_RECORD = "RightRecord";
    private static final String TIMER = "Timer";
    private transient ValueState<L> leftRecordState;
    private transient ValueState<R> rightRecordState;
    private transient ValueState<Long> timerState;
    private final Long leftStateDuration;
    private final Long rightStateDuration;
    private final JoinType joinType;

    public OneToOneJoin(JoinType joinType, Long leftStateDuration, Long rightStateDuration) {
        this.joinType = joinType;
        this.leftStateDuration = leftStateDuration;
        this.rightStateDuration = rightStateDuration;
    }

    @Override
    public void open(OpenContext ctx) {
        RuntimeContext runtimeCtx = getRuntimeContext();
        leftRecordState = runtimeCtx
                .getState(new ValueStateDescriptor<>(LEFT_RECORD, TypeInformation.of(new TypeHint<>() {
                })));
        rightRecordState = runtimeCtx
                .getState(new ValueStateDescriptor<>(RIGHT_RECORD, TypeInformation.of(new TypeHint<>() {
                })));
        timerState = runtimeCtx.getState(new ValueStateDescriptor<>(TIMER, Long.class));

    }

    @Override
    public void processElement1(L input, Context ctx, Collector<Tuple2<K, Tuple2<L, R>>> out) {
        // Validate the input
        if (input == null)
            return;
        R rightValue = rightRecordState.value();
        Long expireTimestamp = timerState.value();
        if (expireTimestamp != null)
            ctx.timerService().deleteProcessingTimeTimer(expireTimestamp);
        if (rightValue != null) {
            out.collect(Tuple2.of(ctx.getCurrentKey(), Tuple2.of(input, rightValue)));
            clearState();
            return;
        }
        long expireTime = ctx.timestamp() + leftStateDuration;
        ctx.timerService().registerEventTimeTimer(expireTime);
        leftRecordState.update(input);
        timerState.update(expireTime);
    }

    @Override
    public void processElement2(R input, Context ctx, Collector<Tuple2<K, Tuple2<L, R>>> out) {
        // Validate the input
        if (input == null)
            return;
        L leftValue = leftRecordState.value();
        Long expireTimestamp = timerState.value();
        if (expireTimestamp != null)
            ctx.timerService().deleteProcessingTimeTimer(expireTimestamp);
        if (leftValue != null) {
            out.collect(Tuple2.of(ctx.getCurrentKey(), Tuple2.of(leftValue, input)));
            clearState();
            return;
        }
        long expireTime = ctx.timestamp() + rightStateDuration;
        ctx.timerService().registerEventTimeTimer(expireTime);
        rightRecordState.update(input);
        timerState.update(expireTime);

    }

    public void onTimer(long timestamp, OnTimerContext ctx, Collector<Tuple2<K, Tuple2<L, R>>> out) {
        if (joinType == JoinType.LEFT) {
            L leftValue = leftRecordState.value();
            if (leftValue != null)
                out.collect(Tuple2.of(ctx.getCurrentKey(), Tuple2.of(leftValue, null)));
        } else if (joinType == JoinType.RIGHT) {
            R rightValue = rightRecordState.value();
            if (rightValue != null)
                out.collect(Tuple2.of(ctx.getCurrentKey(), Tuple2.of(null, rightValue)));
        }
        clearState();
    }

    private void clearState() {
        leftRecordState.clear();
        rightRecordState.clear();
        timerState.clear();
    }
}
