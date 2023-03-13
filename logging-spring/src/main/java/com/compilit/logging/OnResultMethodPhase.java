package com.compilit.logging;

import com.compilit.logging.api.Event;
import com.compilit.logging.api.Log;
import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;

class OnResultMethodPhase extends MethodPhase {

  @Override
  void next(ProceedingJoinPoint joinPoint, Logger logger, List<Log> logEntries, MethodExecution methodExecution) {
    try {
      var methodName = getMethodName(joinPoint);
      var result = joinPoint.proceed();
      if (containsEvent(logEntries, Event.ON_RESULT)) {
        var logAnnotation = getLogAnnotation(logEntries, Event.ON_RESULT);
        log(logger, getMessage(logAnnotation.message(), logAnnotation.event()), logAnnotation.level(), methodName, result);
      }
      methodExecution.setExecutionResult(result);
    } catch (Throwable e) {
      methodExecution.setException(e);
    }
  }
}
