//package com.gimral.streaming.core.aop.advise;
//
//import com.gimral.streaming.core.logging.LeapRecordMDCInjector;
//import com.gimral.streaming.core.model.LeapInternalRecord;
//import com.gimral.streaming.core.model.LeapRecord;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//
//@Aspect
//public class TracingAspect {
//    @Around("com.gimral.streaming.core.aop.pointcut.LeapRecordProcessPointCut.intercept(joinPoint, record)")
//    public Object trace(ProceedingJoinPoint joinPoint, LeapInternalRecord<?> record) throws Throwable {
////        try (LeapRecordMDCInjector ignored = LeapRecordMDCInjector.putAll(record)) {
////            return joinPoint.proceed();
////        }
//        return joinPoint.proceed();
//    }
//}
