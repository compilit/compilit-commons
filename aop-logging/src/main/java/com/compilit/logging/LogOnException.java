package com.compilit.logging;

import static org.slf4j.event.Level.INFO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.slf4j.event.Level;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface LogOnException {
  Level level() default INFO;
  String message() default "Method %s threw an exception: %s";
  boolean rethrow() default false;
}
