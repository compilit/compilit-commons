package com.compilit.mediator.api.annotations;

import com.compilit.mediator.api.Event;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used to emit bodiless events after the successful execution of the concerning method. This
 * means that the event will only fire if no exception are encountered. But this also means that if you handle all
 * exceptions within the method itself, the event will in fact be fired.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface OnSuccess {

  Class<? extends Event> emit();
}
