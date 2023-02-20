package com.compilit.logging.api;

import static com.compilit.logging.api.DefaultMessages.STARTING_METHOD;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.slf4j.event.Level;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface LogBefore {

  Level level() default Level.INFO;

  String message() default STARTING_METHOD;
}
