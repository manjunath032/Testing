package org.example.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

// @Aspect
// @Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private static final String MDC_FUNCTION = "function";

    // Pointcut: all methods in our application's packages
    @Around("execution(* org.example..*(..)) && !within(org.example.logging..*)")
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
        String functionName = pjp.getSignature().toShortString();
        MDC.put(MDC_FUNCTION, functionName);

        if (logger.isTraceEnabled()) {
            logger.trace("ENTER {}", functionName);
        }

        long start = System.currentTimeMillis();
        try {
            Object result = pjp.proceed();
            long duration = System.currentTimeMillis() - start;
            if (logger.isTraceEnabled()) {
                logger.trace("EXIT {} ({} ms)", functionName, duration);
            }
            return result;
        } catch (Throwable t) {
            if (logger.isTraceEnabled()) {
                logger.trace("EXCEPTION in {}: {}", functionName, t.toString());
            }
            throw t;
        } finally {
            MDC.remove(MDC_FUNCTION);
        }
    }
}
