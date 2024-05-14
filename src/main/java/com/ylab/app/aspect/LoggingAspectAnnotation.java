package com.ylab.app.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * LoggingAspectAnnotation class provides an aspect for logging method executions of types annotated with {@link EnableLogging}.
 * It intercepts method invocations and logs relevant information such as the timestamp, method signature, and execution time.
 *
 * This aspect class is annotated with {@link Aspect} and {@link Component} to mark it as an aspect component for Spring AOP.
 * It contains a pointcut and advice to log the execution time for the methods in types annotated with {@link EnableLogging}.
 *
 * Example of usage:
 * <pre>
 * {@code
 * {@literal @}EnableLogging
 * public class MyLoggedService {
 *     //...
 * }
 * }
 * </pre>
 *
 * @author razlivinsky
 * @since 05.05.2024
 */
@Aspect
public class LoggingAspectAnnotation {
    /**
     * Defines a pointcut to target method executions within types annotated with {@link EnableLogging}.
     */
    @Pointcut("within(@com.ylab.app.aspect.EnableLogging *) && execution(* * (..))")
    public void enableLoggingPointcut() {}

    /**
     * Logs the execution time for the intercepted methods and outputs the information to the console.
     *
     * @param joinPoint the {@link ProceedingJoinPoint} representing the intercepted method invocation
     * @return the result of the method invocation
     * @throws Throwable if an error occurs during the method invocation
     */
    @Around("enableLoggingPointcut()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentTime.format(formatter);
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        System.out.println("\u001B[34m" + formattedDateTime + " "
                + joinPoint.getSignature().getName() + " "
                + joinPoint.getSignature().toString() + " "
                + Arrays.toString(joinPoint.getArgs()) +
                " executed in " + executionTime + "ms" + "\u001B[0m");
        return proceed;
    }
}