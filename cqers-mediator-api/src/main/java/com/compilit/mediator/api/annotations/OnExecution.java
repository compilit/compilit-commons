package com.compilit.mediator.api.annotations;

import com.compilit.mediator.api.Event;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used to emit bodiless events before the execution of the concerning method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface OnExecution {

  Class<? extends Event> emit();
}
