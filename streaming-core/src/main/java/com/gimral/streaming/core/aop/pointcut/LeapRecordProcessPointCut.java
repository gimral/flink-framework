package com.gimral.streaming.core.aop.pointcut;

import com.gimral.streaming.core.model.LeapRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Pointcut;

public class LeapRecordProcessPointCut {
    @Pointcut(
            // Matches MapFunction,RichMapFunction, FlatMapFunction, RichFlatMapFunction,FilterFunction, RichFilterFunction
            "(execution(* org.apache.flink.api.common.functions.MapFunction+.map(..)) && args(record)) || " +
                    "(execution(* org.apache.flink.api.common.functions.FlatMapFunction+.flatMap(..)) && args(record, *)) || " +
                    "(execution(* org.apache.flink.api.common.functions.FilterFunction+.filter(..)) && args(record, *))")
                    //Matches CoProcessFunction,KeyedCoProcessFunction
//                    "(execution(* org.apache.flink.streaming.api.functions.co.CoProcessFunction+.processElement1(..)) && args(record, *)) || " +
//                    "(execution(* org.apache.flink.streaming.api.functions.co.CoProcessFunction+.processElement2(..)) && args(record, *)) || " +
//                    "(execution(* org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction+.processElement1(..)) && args(record, *)) || " +
//                    "(execution(* org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction+.processElement2(..)) && args(record, *))")
    public void intercept(ProceedingJoinPoint joinPoint, LeapRecord<?> record) {}
}
