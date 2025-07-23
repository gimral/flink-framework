
package com.gimral.streaming.core.aop.advise.logging;

import com.gimral.streaming.core.logging.LeapRecordMDCInjector;
import com.gimral.streaming.core.model.LeapRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Injects LeapRecord and LeapEvent fields into the MDC for logging context.
 */
@Aspect
public class LoggingAspect {

    @Around("com.gimral.streaming.core.aop.LeapRecordProcessPointCut.intercept(joinPoint, record)")
    public Object log(ProceedingJoinPoint joinPoint, LeapRecord<?> record) throws Throwable {
        try (LeapRecordMDCInjector ignored = LeapRecordMDCInjector.putAll(record)) {
            return joinPoint.proceed();
        }
    }
}
