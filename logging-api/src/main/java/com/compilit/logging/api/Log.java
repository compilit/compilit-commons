package com.compilit.logging.api;

import static com.compilit.logging.api.DefaultMessages.EXCEPTION_THROWN;
import static com.compilit.logging.api.DefaultMessages.FINISHED_METHOD;
import static com.compilit.logging.api.DefaultMessages.STARTING_METHOD;
import static com.compilit.logging.api.LogScope.ALL;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.slf4j.event.Level;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {

  LogScope scope() default ALL;

  Level level() default Level.INFO;

  String before() default STARTING_METHOD;

  String after() default FINISHED_METHOD;

  String onException() default EXCEPTION_THROWN;

  boolean rethrow() default false;
}
