package com.ylab.app.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Represents an auditing mechanism for user actions in the system.
 *
 * @author razlivinsky
 * @since 27.01.2024
 */
@Aspect
public class Audition {
    @Pointcut("within(@com.ylab.app.aspect.LogExecution *) && execution(* * (..))")
    public void annotatedByAudit() {}

    @Around("annotatedByAudit()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println(joinPoint.getSignature());
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis() - start;
        System.out.println(joinPoint.getSignature() + " " + end);
        return result;
    }
}