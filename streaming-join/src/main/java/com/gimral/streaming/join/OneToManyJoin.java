package com.gimral.streaming.join;

import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.state.v2.ListState;
import org.apache.flink.api.common.state.v2.ListStateDescriptor;
import org.apache.flink.api.common.state.v2.ValueState;
import org.apache.flink.api.common.state.v2.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import com.gimral.streaming.core.functions.LeapKeyedCoProcessFunction;

public class OneToManyJoin<K, L, R> extends LeapKeyedCoProcessFunction<K, L, R, Tuple2<K, Tuple2<L, R>>> {
    private static final String LEFT_RECORD = "LeftRecord";
    private static final String RIGHT_RECORDS = "RightRecords";
    private static final String LEFT_TIMER = "LeftTimer";
    private static final String RIGHT_TIMER = "RightTimer";
    private static final String JOINED = "Joined";
    private transient ValueState<L> leftRecordState;
    private transient ListState<R> rightRecordsState;
    private transient ValueState<Long> leftTimerState;
    private transient ValueState<Long> rightTimerState;
    private transient ValueState<Boolean> joinedState;
    private final Long leftStateDuration;
    private final Long rightStateDuration;
    private final JoinType joinType;

    public OneToManyJoin(JoinType joinType, Long leftStateDuration, Long rightStateDuration) {
        this.joinType = joinType;
        this.leftStateDuration = leftStateDuration;
        this.rightStateDuration = rightStateDuration;
    }

    @Override
    public void innerOpen(OpenContext ctx) {
        RuntimeContext runtimeCtx = getRuntimeContext();
        leftRecordState = runtimeCtx
                .getState(new ValueStateDescriptor<>(LEFT_RECORD, TypeInformation.of(new TypeHint<>() {
                })));
        rightRecordsState = runtimeCtx
                .getListState(new ListStateDescriptor<>(RIGHT_RECORDS, TypeInformation.of(new TypeHint<>() {
                })));
        leftTimerState = runtimeCtx.getState(new ValueStateDescriptor<>(LEFT_TIMER, Long.class));
        rightTimerState = runtimeCtx.getState(new ValueStateDescriptor<>(RIGHT_TIMER, Long.class));
        joinedState = runtimeCtx.getState(new ValueStateDescriptor<>(JOINED, Boolean.class));
    }

    @Override
    public void innerProcessElement1(L input, Context ctx, Collector<Tuple2<K, Tuple2<L, R>>> out) throws Exception {
        if (input == null)
            return;
        Iterable<R> rightValues = rightRecordsState.get();
        boolean matched = false;
        for (R right : rightValues) {
            out.collect(Tuple2.of(ctx.getCurrentKey(), Tuple2.of(input, right)));
            matched = true;
        }
        if (matched) {
            joinedState.update(true);
            Long rightExpireTime = rightTimerState.value();
            if (rightExpireTime != null)
                ctx.timerService().deleteProcessingTimeTimer(rightExpireTime);
            rightRecordsState.clear();
        }
        Long leftExpireTime = leftTimerState.value();
        if (leftExpireTime != null)
            ctx.timerService().deleteProcessingTimeTimer(leftExpireTime);
        long expireTime = ctx.timestamp() + leftStateDuration;
        ctx.timerService().registerEventTimeTimer(expireTime);
        leftRecordState.update(input);
        leftTimerState.update(expireTime);
    }

    @Override
    public void innerProcessElement2(R input, Context ctx, Collector<Tuple2<K, Tuple2<L, R>>> out) throws Exception {
        if (input == null)
            return;
        L leftValue = leftRecordState.value();
        if (leftValue != null) {
            out.collect(Tuple2.of(ctx.getCurrentKey(), Tuple2.of(leftValue, input)));
            joinedState.update(true);
            return;
        }
        // TODO: We need to introduce the maximun number records that could be stored in
        // the state
        rightRecordsState.add(input);
        Long rightExpireTime = rightTimerState.value();
        if (rightExpireTime != null)
            ctx.timerService().deleteProcessingTimeTimer(rightExpireTime);
        long expireTime = ctx.timestamp() + rightStateDuration;
        ctx.timerService().registerEventTimeTimer(expireTime);
        rightTimerState.update(expireTime);
    }

    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<Tuple2<K, Tuple2<L, R>>> out) throws Exception {
        Boolean joined = joinedState.value();
        if (joined != null && joined) {
            clearState();
            return;
        }
        if (joinType == JoinType.LEFT) {
            L leftValue = leftRecordState.value();
            if (leftValue != null)
                out.collect(Tuple2.of(ctx.getCurrentKey(), Tuple2.of(leftValue, null)));
        } else if (joinType == JoinType.RIGHT) {
            Iterable<R> rightValues = rightRecordsState.get();
            for (R right : rightValues) {
                out.collect(Tuple2.of(ctx.getCurrentKey(), Tuple2.of(null, right)));
            }
        }
        clearState();
    }

    private void clearState() throws Exception {
        leftRecordState.clear();
        rightRecordsState.clear();
        leftTimerState.clear();
        rightTimerState.clear();
        joinedState.clear();
    }
}
