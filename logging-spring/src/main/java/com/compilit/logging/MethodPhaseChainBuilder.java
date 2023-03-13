package com.compilit.logging;

import com.compilit.logging.api.Log;
import com.compilit.logging.api.Logged;
import java.util.ArrayList;
import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
class MethodPhaseChainBuilder {

  private final List<MethodPhase> methodPhases = new ArrayList<>();
  private boolean shouldRethrowExceptions;

  public MethodPhaseChainBuilder addPhase(MethodPhase methodPhase) {
    methodPhases.add(methodPhase);
    return this;
  }

  public Object process(ProceedingJoinPoint joinPoint, Logger logger, List<Log> logEntries, Logged annotation) throws Throwable {
    var methodExecution = new MethodExecution();
    methodPhases.forEach(entry -> entry.next(joinPoint, logger, logEntries, methodExecution));
    if (methodExecution.isExceptionThrown() && shouldRethrowExceptions) {
        throw methodExecution.getException();
    }
    return methodExecution.getExecutionResult();
  }

  public MethodPhaseChainBuilder configure(LoggingConfiguration loggingConfiguration) {
    this.shouldRethrowExceptions = loggingConfiguration.isRethrowExceptions();
    return this;
  }
}
