package com.compilit.mediator;

import com.compilit.mediator.api.Event;
import com.compilit.mediator.api.EventEmitter;
import com.compilit.mediator.api.annotations.OnExecution;
import com.compilit.mediator.api.annotations.OnSuccess;
import java.util.Arrays;
import java.util.function.Predicate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EmittableAspect {
  private final EventEmitter eventEmitter;

  public EmittableAspect(EventEmitter eventEmitter) {this.eventEmitter = eventEmitter;}

  @Before(value = "@annotation(annotation)")
  public void onExecution(JoinPoint joinPoint, OnExecution annotation) {
    emitEvent(joinPoint);
  }

  @After(value = "@annotation(annotation)")
  public void onSuccess(JoinPoint joinPoint, OnSuccess annotation) {
    emitEvent(joinPoint);
  }

  private void emitEvent(JoinPoint joinPoint) {
    Arrays.stream(joinPoint.getArgs())
          .filter(anyEventArgument())
          .map(x -> (Event)x)
          .forEach(eventEmitter::emit);
  }

  private Predicate<? super Object> anyEventArgument() {
    return object -> object instanceof Event;
  }
}
