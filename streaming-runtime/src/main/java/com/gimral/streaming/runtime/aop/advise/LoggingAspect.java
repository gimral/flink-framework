package com.gimral.streaming.runtime.aop.advise;

import com.gimral.streaming.core.model.LeapRecord;
import com.gimral.streaming.runtime.logging.LeapRecordMDCInjector;
import org.apache.flink.streaming.runtime.streamrecord.StreamRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/** Injects LeapRecord and LeapEvent fields into the MDC for logging context. */
@Aspect
public class LoggingAspect {

    private final Logger logger = LogManager.getLogger(LoggingAspect.class);

    @Around(
            "com.gimral.streaming.runtime.aop.pointcut.LeapRecordProcessPointCut.interceptOneInputStreamOperator(joinPoint,"
                + " element) ||"
                + " com.gimral.streaming.runtime.aop.pointcut.LeapRecordProcessPointCut.interceptTwoInputStreamOperator(joinPoint,"
                + " element)")
    public Object interceptStreamOperator(ProceedingJoinPoint joinPoint, StreamRecord<?> element)
            throws Throwable {
        if (!(element.getValue() instanceof LeapRecord<?> leapRecord)) return joinPoint.proceed();

        try (LeapRecordMDCInjector ignored = LeapRecordMDCInjector.putAll(leapRecord)) {
            long startTime = System.nanoTime();

            Object retVal = joinPoint.proceed();

            long endTime = System.nanoTime();
            long durationInNanos = endTime - startTime;

            logger.trace(
                    "Method {} executed in {} nanoseconds",
                    joinPoint.getSignature().toShortString(),
                    durationInNanos);

            return retVal;
        }
    }
}
