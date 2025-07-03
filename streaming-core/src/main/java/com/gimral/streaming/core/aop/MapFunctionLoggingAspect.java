package com.gimral.streaming.core.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.gimral.streaming.core.model.LeapRecord;

/**
 * AspectJ interceptor for all Flink MapFunction implementations.
 * Logs before and after the map method execution.
 * Adds LeapRecord.urc to ThreadContext if present.
 */
@Aspect
public class MapFunctionLoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(MapFunctionLoggingAspect.class);
    /**
     * Intercept all map methods of classes implementing
     * org.apache.flink.api.common.functions.MapFunction
     */
    @Around("execution(* org.apache.flink.api.common.functions.MapFunction+.map(..)) || " +
            "execution(* org.apache.flink.api.common.functions.FlatMapFunction+.flatMap(..))")
    public Object logAroundMap(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object param = args.length > 0 ? args[0] : null;
        if (param instanceof LeapRecord) {
            String urc = ((LeapRecord<?>) param).getUrc();
            try (MDC.MDCCloseable ignored = MDC.putCloseable("urc", urc)) {
                return joinPoint.proceed();
            }
        } else {
            // If not a LeapRecord, proceed without adding urc to MDC
            return joinPoint.proceed();
        }
    }
}
