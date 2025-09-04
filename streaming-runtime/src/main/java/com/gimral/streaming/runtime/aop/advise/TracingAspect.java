package com.gimral.streaming.runtime.aop.advise; // package com.gimral.streaming.core.aop.advise;
//
// import com.gimral.streaming.core.model.LeapEvent;
// import com.gimral.streaming.core.model.LeapRecord;
// import com.gimral.streaming.core.model.LeapRecordConstants;
// import io.opentelemetry.api.GlobalOpenTelemetry;
// import io.opentelemetry.api.trace.Span;
// import io.opentelemetry.api.trace.StatusCode;
// import io.opentelemetry.api.trace.Tracer;
// import io.opentelemetry.context.Context;
// import io.opentelemetry.context.Scope;
// import org.apache.flink.streaming.runtime.streamrecord.StreamRecord;
// import org.aspectj.lang.ProceedingJoinPoint;
// import org.aspectj.lang.annotation.Around;
// import org.aspectj.lang.annotation.Aspect;
//
/// ** Injects LeapRecord and LeapEvent fields into the MDC for logging context. */
// @Aspect
// public class TracingAspect {
//    private static Tracer tracer;
//
//    static {
//        // Initialize OpenTelemetry (configure exporter in your app or environment)
//        tracer = GlobalOpenTelemetry.getTracer("flink-tracer");
//    }
//
//    @Around(
//
// "com.gimral.streaming.core.runtime.pointcut.LeapRecordProcessPointCut.interceptOneInputStreamOperator(joinPoint,"
//                + " element)"
//                + "
// ||com.gimral.streaming.runtime.aop.pointcut.LeapRecordProcessPointCut.interceptTwoInputStreamOperator(joinPoint,"
//                + " element)")
//    public Object interceptStreamOperator(ProceedingJoinPoint joinPoint, StreamRecord<?> element)
//            throws Throwable {
//        if (tracer == null) return joinPoint.proceed();
//        if (!(element.getValue() instanceof LeapRecord<?> leapRecord)) return joinPoint.proceed();
//        // TODO: Add metadata if not LeapEvent
//        if (!(leapRecord.getValue() instanceof LeapEvent<?> event)) return joinPoint.proceed();
//        String methodName = joinPoint.getSignature().getName();
//        // Create span
//        Span span =
//                tracer.spanBuilder("process by " + methodName)
//                        .setParent(Context.current())
//                        .startSpan();
//
//        try (Scope ignored = span.makeCurrent()) {
//            // Add attributes
//            //            span.setAttribute("input.value", leapRecord.toString());
//            // messaging.message, messaging.source, ..
//            span.setAttribute(LeapRecordConstants.URC, event.getUrc());
//            span.setAttribute("target.class", joinPoint.getTarget().getClass().getName());
//            // Proceed with map execution
//            // span.setAttribute("output.value", result != null ? result.toString() : "null");
//            return joinPoint.proceed();
//        } catch (Throwable t) {
//            // Record exception in span
//            span.recordException(t);
//            span.setStatus(StatusCode.ERROR, "Function failed");
//            throw t;
//        } finally {
//            span.end();
//        }
//    }
//
//    public static void setTracer(Tracer t) {
//        tracer = t;
//    }
// }
