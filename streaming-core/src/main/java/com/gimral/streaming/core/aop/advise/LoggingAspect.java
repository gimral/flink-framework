
package com.gimral.streaming.core.aop.advise;

import com.gimral.streaming.core.logging.LeapRecordMDCInjector;
import com.gimral.streaming.core.model.LeapRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Injects LeapRecord and LeapEvent fields into the MDC for logging context.
 */
@Aspect
public class LoggingAspect {

    private final Logger logger = LogManager.getLogger(LoggingAspect.class.getName());

    @Around("com.gimral.streaming.core.aop.pointcut.LeapRecordProcessPointCut.intercept(joinPoint, record)")
    public Object loggingIntercept(ProceedingJoinPoint joinPoint, LeapRecord<?> record) throws Throwable {
        try (LeapRecordMDCInjector ignored = LeapRecordMDCInjector.putAll(record)) {
            long startTime = System.nanoTime();

            Object retVal = joinPoint.proceed();

            long endTime = System.nanoTime();
            long durationInNanos = endTime - startTime;

            logger.trace("Method {} executed in {} nanoseconds with record: {}",
                         joinPoint.getSignature().toShortString(),
                         durationInNanos,
                         record);

            return retVal;
        }
    }
}
