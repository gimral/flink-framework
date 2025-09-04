package com.gimral.streaming.runtime.aop.pointcut;

import org.apache.flink.streaming.runtime.streamrecord.StreamRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Pointcut;

public class LeapRecordProcessPointCut {
    @Pointcut(
            // Matches MapFunction,RichMapFunction, FlatMapFunction,
            // RichFlatMapFunction,FilterFunction,
            // RichFilterFunction,
            // Project, SinkFunction, MapPartition, ProcessFunction, KeyedProcessFunction
            "(   (execution(*"
                + " org.apache.flink.streaming.api.operators.OneInputStreamOperator+.processElement(..))"
                + " && args(element))) && within(org.apache.flink.streaming.api.operators.*)")
    public void interceptOneInputStreamOperator(
            ProceedingJoinPoint joinPoint, StreamRecord<?> element) {}

    @Pointcut(
            // KeyedCoProcessOperator,CoStreamMap,CoStreamFlatMap
            "(   (execution(*"
                + " org.apache.flink.streaming.api.operators.TwoInputStreamOperator+.processElement*(..))"
                + " && args(element))) && within(org.apache.flink.streaming.api.operators.*..*)")
    public void interceptTwoInputStreamOperator(
            ProceedingJoinPoint joinPoint, StreamRecord<?> element) {}
}
