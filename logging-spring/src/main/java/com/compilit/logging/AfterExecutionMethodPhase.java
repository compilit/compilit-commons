package com.compilit.logging;

import com.compilit.logging.api.Event;
import com.compilit.logging.api.Log;
import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;

class AfterExecutionMethodPhase extends MethodPhase {

  @Override
  void next(ProceedingJoinPoint joinPoint, Logger logger, List<Log> logEntries, MethodExecution methodExecution) {
    var methodName = getMethodName(joinPoint);
    if (containsEvent(logEntries, Event.ON_FINISHED)) {
      var logAnnotation = getLogAnnotation(logEntries, Event.ON_FINISHED);
      log(logger, getMessage(logAnnotation.message(), logAnnotation.event()), logAnnotation.level(), methodName);
    }
  }
}
