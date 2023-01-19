package com.compilit.logging;

import static com.compilit.logging.Scope.ALL;
import static org.slf4j.event.Level.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.slf4j.event.Level;
import org.springframework.core.annotation.AliasFor;

@LogBefore
@LogAfter
@LogOnException
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {
  Scope scope() default ALL;
  Level level() default INFO;
  @AliasFor(annotation = LogBefore.class, attribute = "message")
  String before() default "Starting method %s";
  @AliasFor(annotation = LogAfter.class, attribute = "message")
  String after() default "Finished method %s";
  @AliasFor(annotation = LogOnException.class, attribute = "message")
  String onException() default "Method %s threw an exception: %s";
  @AliasFor(annotation = LogOnException.class, attribute = "rethrow")
  boolean rethrow() default false;
}
