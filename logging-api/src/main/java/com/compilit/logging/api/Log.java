package com.compilit.logging.api;

import static com.compilit.logging.api.Scope.ALL;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.slf4j.event.Level;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {

  Scope scope() default ALL;

  Level level() default Level.INFO;

  String before() default "Starting method %s";

  String after() default "Finished method %s";

  String onException() default "Method %s threw an exception: %s";

  boolean rethrow() default false;
}
