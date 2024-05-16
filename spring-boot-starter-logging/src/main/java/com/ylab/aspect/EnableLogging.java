package com.ylab.aspect;

import com.ylab.config.LoggingAspectConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * EnableLogging annotation is used to enable logging aspect for annotated types.
 * By flagging a type with this annotation, it enables the LoggingAspectAnnotation to intercept
 * the annotated type's method invocations and log relevant information.
 *
 * This annotation is marked to be retained at runtime to ensure that the associated LoggingAspectAnnotation
 * is activated when the code is executed.
 *
 * Example of usage:
 *
 * <pre>
 * {@literal @}EnableLogging
 * public class MyLoggedService {
 *     //...
 * }
 * </pre>
 *
 * @author razlivinsky
 * @since 22.02.2024
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LoggingAspectConfiguration.class)
public @interface EnableLogging {
}
