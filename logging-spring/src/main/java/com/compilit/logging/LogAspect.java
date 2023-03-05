package com.compilit.logging;

import com.compilit.logging.api.Log;
import com.compilit.logging.api.LogAfter;
import com.compilit.logging.api.LogBefore;
import com.compilit.logging.api.LogOnException;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

  private final Map<ProceedingJoinPoint, String> methodNames = new HashMap<>();

  @Around(value = "@annotation(annotation)")
  public Object logBeforeExecution(ProceedingJoinPoint joinPoint, LogBefore annotation) throws Throwable {
    var logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
    var methodName = getMethodName(joinPoint);
    return before(joinPoint, annotation.message(), logger, methodName, annotation.level());
  }

  @Around(value = "@annotation(annotation)")
  public Object logAfterExecution(ProceedingJoinPoint joinPoint, LogAfter annotation) throws Throwable {
    var logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
    var methodName = getMethodName(joinPoint);
    return after(joinPoint, annotation.message(), logger, methodName, annotation.level());
  }

  @Around(value = "@annotation(annotation)")
  public Object logOnException(ProceedingJoinPoint joinPoint, LogOnException annotation) throws Throwable {
    var logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
    var methodName = getMethodName(joinPoint);
    return exception(joinPoint, annotation.message(), logger, methodName, annotation.rethrow());
  }

  @Around(value = "@annotation(annotation)")
  public Object logAll(ProceedingJoinPoint joinPoint, Log annotation) throws Throwable {
    var logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
    var methodName = getMethodName(joinPoint);
    return all(joinPoint, annotation, logger, methodName);
  }

  private String getMethodName(ProceedingJoinPoint joinPoint) {
    String methodName;
    if (methodNames.containsKey(joinPoint)) {
      methodName = methodNames.get(joinPoint);
    } else {
      methodName = joinPoint.getSignature().getName();
      methodNames.put(joinPoint, methodName);
    }
    return methodName;
  }

  private static Object all(ProceedingJoinPoint joinPoint, Log annotation, Logger logger, String methodName)
    throws Throwable {
    try {
      log(logger, createMessage(annotation.before(), methodName), annotation.level());
      var result = joinPoint.proceed();
      log(logger, createMessage(annotation.after(), methodName), annotation.level());
      return result;
    } catch (Exception e) {
      log(logger, createMessage(annotation.onException(), methodName), Level.ERROR);
      if (annotation.rethrow()) {
        throw e;
      }
      return null;
    }
  }

  private static Object exception(ProceedingJoinPoint joinPoint, String message, Logger logger, String methodName,
                                  boolean rethrow)
    throws Throwable {
    try {
      return joinPoint.proceed();
    } catch (Exception e) {
      logger.error(createMessage(message, methodName, e.getMessage()), e);
      if (rethrow) {
        throw e;
      }
      return null;
    }
  }

  private static Object after(ProceedingJoinPoint joinPoint,
                              String message,
                              Logger logger,
                              String methodName,
                              Level level)
    throws Throwable {
    var result = joinPoint.proceed();
    log(logger, createMessage(message, methodName), level);
    return result;
  }

  private static Object before(ProceedingJoinPoint joinPoint,
                               String message,
                               Logger logger,
                               String methodName,
                               Level level)
    throws Throwable {
    log(logger, createMessage(message, methodName), level);
    return joinPoint.proceed();
  }

  private static String createMessage(String message, String... args) {
    return String.format(message, (Object[]) args);
  }

  private static void log(Logger logger, String message, Level level) {
    switch (level) {
      case ERROR -> logger.error(message);
      case WARN -> logger.warn(message);
      case INFO -> logger.info(message);
      case DEBUG -> logger.debug(message);
      case TRACE -> logger.trace(message);
    }
  }
}
