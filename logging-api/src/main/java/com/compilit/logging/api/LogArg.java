package com.compilit.logging.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.slf4j.event.Level;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface LogArg {
  Level level() default Level.INFO;
  String message() default DefaultMessages.ARGUMENT_LOG;
}
