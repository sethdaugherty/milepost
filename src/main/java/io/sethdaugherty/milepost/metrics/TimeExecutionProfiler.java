package io.sethdaugherty.milepost.metrics;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TimeExecutionProfiler {

    private static final Logger logger = LoggerFactory.getLogger(TimeExecutionProfiler.class);

    @Around("execution(* io.sethdaugherty.milepost.api..*.*(..))")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        String methodName = pjp.getSignature().getName();
        logger.info("ServicesProfiler.profile(): Going to call the method: {}.{}", pjp.getSignature().getDeclaringTypeName(), methodName);
        Object output = pjp.proceed();
        logger.info("ServicesProfiler.profile(): Method (" + methodName + ") execution completed.");
        long elapsedTime = System.currentTimeMillis() - start;
        logger.info("ServicesProfiler.profile(): Method (" + methodName + ") execution time: " + elapsedTime + " milliseconds.");

        return output;
    }

    @After("execution(* io.sethdaugherty.milepost.api..*.*(..))")
    public void profileMemory() {
        logger.info("JVM memory in use = {}", (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
    }
}