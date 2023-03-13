package com.compilit.logging;

import com.compilit.logging.api.Event;
import com.compilit.logging.api.Log;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.event.Level;

abstract class MethodPhase {

  private final Map<ProceedingJoinPoint, String> methodNames = new HashMap<>();
  
  abstract void next(ProceedingJoinPoint joinPoint, Logger logger, List<Log> logEntries, MethodExecution methodExecution);

  protected static String getMessage(String provided, Event event) {
    if (StringUtils.isNotBlank(provided))
      return provided;
    else return event.getDefaultMessage();
  }

  protected static boolean containsEvent(List<Log> logEntries, Event event) {
    return logEntries.stream().anyMatch(logEntry -> logEntry.event().equals(event));
  }

  protected static Log getLogAnnotation(List<Log> logEntries, Event event) {
    return logEntries.stream().filter(logEntry -> logEntry.event().equals(event)).findFirst().orElseThrow();
  }

  protected String getMethodName(ProceedingJoinPoint joinPoint) {
    String methodName;
    if (methodNames.containsKey(joinPoint)) {
      methodName = methodNames.get(joinPoint);
    } else {
      methodName = joinPoint.getSignature().getName();
      methodNames.put(joinPoint, methodName);
    }
    return methodName;
  }
  protected static void log(Logger logger, String message, Level level, Object... args) {
    switch (level) {
      case ERROR -> logger.error(message, args);
      case WARN -> logger.warn(message, args);
      case INFO -> logger.info(message, args);
      case DEBUG -> logger.debug(message, args);
      case TRACE -> logger.trace(message, args);
    }
  }
}
