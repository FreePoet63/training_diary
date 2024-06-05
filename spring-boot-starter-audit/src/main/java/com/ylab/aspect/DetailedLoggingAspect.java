package com.ylab.aspect;

import com.ylab.model.AuditModel;
import com.ylab.repository.AuditDao;
import com.ylab.repository.impl.AuditDaoImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * DetailedLoggingAspect class
 *
 * This class represents an aspect that provides detailed logging functionality for methods
 * within the com.ylab.app package. It utilizes Aspect-Oriented Programming (AOP) to intercept
 * method execution and log information before, after successful completion, or in case of failure.
 *
 * @author razlivinsky
 * @since 14.05.2024
 */
@Aspect
@Component
public class DetailedLoggingAspect {
    private final AuditDao auditDao;

    /**
     * Instantiates a new detailed logging aspect.
     *
     * @param jdbcTemplate the jdbc template used for database operations
     */
    public DetailedLoggingAspect(JdbcTemplate jdbcTemplate) {
        this.auditDao = new AuditDaoImpl(jdbcTemplate);
    }

    /**
     * Intercepts the method execution before it begins to log the input parameters.
     *
     * @param joinPoint the join point at which this advice is being executed
     */
    @Before("execution(* com.ylab.app.service..*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String[] parameterNames = signature.getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();

        StringBuilder params = new StringBuilder();
        for (int i = 0; i < parameterNames.length; i++) {
            params.append(parameterNames[i]).append(":").append(parameterValues[i]);
            if (i < parameterNames.length - 1) {
                params.append(", ");
            }
        }
        String begin = ("BEGIN: " + methodName + "(" + params + ")");
        AuditModel audit = new AuditModel(begin);
        auditDao.sendMessage(audit);
        System.out.println(auditDao.getMessage());
    }

    /**
     * Intercepts the method execution after it successfully returns to log the result.
     *
     * @param joinPoint the join point at which this advice is being executed
     * @param result the result returned by the intercepted method
     */
    @AfterReturning(
            pointcut = "execution(* com.ylab.app.service..*(..))",
            returning = "result"
    )
    public void afterReturningMethod(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String end = ("SUCCESS: " + methodName + ", RESULT: " + result);
        AuditModel audit = new AuditModel(end);
        auditDao.sendMessage(audit);
        System.out.println(auditDao.getMessage());
    }

    /**
     * Intercepts the method execution after it throws an exception to log the error message.
     *
     * @param joinPoint the join point at which this advice is being executed
     * @param error the exception thrown by the intercepted method
     */
    @AfterThrowing(
            pointcut = "execution(* com.ylab.app.service..*(..))",
            throwing = "error"
    )
    public void afterThrowingMethod(JoinPoint joinPoint, Throwable error) {
        String methodName = joinPoint.getSignature().getName();
        String endException = ("FAILURE: " + methodName + ", ERROR: " + error.getMessage());
        AuditModel audit = new AuditModel(endException);
        auditDao.sendMessage(audit);
        System.out.println(auditDao.getMessage());
    }
}