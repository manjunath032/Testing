package org.example.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private static final String MDC_FUNCTION = "function";

    // Pointcut: all methods in our application's packages
    @Around("execution(* org.example..*(..)) && !within(org.example.logging..*)")
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
        String functionName = pjp.getSignature().toShortString();
        MDC.put(MDC_FUNCTION, functionName);

        if (logger.isInfoEnabled()) {
            logger.info("ENTER {}", functionName);
        }
        if (logger.isDebugEnabled()) {
            try {
                Object[] args = pjp.getArgs();
                logger.debug("ARGS {} -> {}", functionName, args == null ? "[]" : java.util.Arrays.toString(args));
            } catch (Exception e) {
                logger.debug("Failed to log args for {}", functionName, e);
            }
        }

        long start = System.currentTimeMillis();
        try {
            Object result = pjp.proceed();
            long duration = System.currentTimeMillis() - start;
            if (logger.isInfoEnabled()) {
                logger.info("EXIT {} ({} ms)", functionName, duration);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("RESULT {} -> {}", functionName, result);
            }
            return result;
        } catch (Throwable t) {
            logger.error("EXCEPTION in {}: {}", functionName, t.toString(), t);
            throw t;
        } finally {
            MDC.remove(MDC_FUNCTION);
        }
    }
}

