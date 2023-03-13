package com.compilit.logging;

import com.compilit.logging.api.Event;
import com.compilit.logging.api.Log;
import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;

class OnCallMethodPhase extends MethodPhase {

  @Override
  void next(ProceedingJoinPoint joinPoint, Logger logger, List<Log> logEntries, MethodExecution methodExecution) {
    var methodName = getMethodName(joinPoint);
    if (containsEvent(logEntries, Event.ON_CALLED) && joinPoint.getArgs().length > 0) {
      var logAnnotation = getLogAnnotation(logEntries, Event.ON_CALLED);
      String argumentsMessage = createArgumentsMessage(joinPoint, logAnnotation);
      var varargs = new Object[joinPoint.getArgs().length + 1];
      System.arraycopy(joinPoint.getArgs(), 0, varargs, 1, varargs.length - 1);
      varargs[0] = methodName;
      log(logger, argumentsMessage, logAnnotation.level(), varargs);
    }
  }

  private static String createArgumentsMessage(ProceedingJoinPoint joinPoint, Log logAnnotation) {
    var stringBuilder = new StringBuilder();
    stringBuilder.append(getMessage(logAnnotation.message(), logAnnotation.event()));
    for (int index = 0; index < joinPoint.getArgs().length; index++) {
      var argument = joinPoint.getArgs()[index];
      stringBuilder.append(String.format("[%s (%s)]: {} ", index, argument.getClass()));
    }
    return stringBuilder.toString();
  }
}
