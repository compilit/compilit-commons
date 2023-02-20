package com.compilit.logging.api;

import static com.compilit.logging.api.DefaultMessages.EXCEPTION_THROWN;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.slf4j.event.Level;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface LogOnException {

  Level level() default Level.INFO;

  String message() default EXCEPTION_THROWN;

  boolean rethrow() default false;
}
