package com.compilit.logging;

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
  public Object logBeforeExecutionTime(ProceedingJoinPoint joinPoint, LogBefore annotation) throws Throwable {
    var logger =  LoggerFactory.getLogger(joinPoint.getTarget().getClass());
    var methodName = getMethodName(joinPoint);
    return before(joinPoint, annotation.message(), logger, methodName, annotation.level());
  }

  @Around(value = "@annotation(annotation)")
  public Object logAfterExecutionTime(ProceedingJoinPoint joinPoint, LogAfter annotation) throws Throwable {
    var logger =  LoggerFactory.getLogger(joinPoint.getTarget().getClass());
    var methodName = getMethodName(joinPoint);
    return after(joinPoint, annotation.message(), logger, methodName, annotation.level());
  }

  @Around(value = "@annotation(annotation)")
  public Object logOnExceptionExecutionTime(ProceedingJoinPoint joinPoint, LogOnException annotation) throws Throwable {
    var logger =  LoggerFactory.getLogger(joinPoint.getTarget().getClass());
    var methodName = getMethodName(joinPoint);
    return exception(joinPoint, annotation.message(), logger, methodName);
  }

  @Around(value = "@annotation(annotation)")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint, Log annotation) throws Throwable {
    var logger =  LoggerFactory.getLogger(joinPoint.getTarget().getClass());
    var methodName = getMethodName(joinPoint);
    return switch (annotation.scope()) {
      case BEFORE -> before(joinPoint, annotation.before(), logger, methodName, annotation.level());
      case AFTER -> after(joinPoint, annotation.after(), logger, methodName, annotation.level());
      case EXCEPTION -> exception(joinPoint, annotation.onException(), logger, methodName);
      case ALL -> all(joinPoint, annotation, logger, methodName);
    };
  }

  private static Object all(ProceedingJoinPoint joinPoint, Log annotation, Logger logger, String methodName)
    throws Throwable {
    try {
      log(logger, createMessage(annotation.before(), methodName), annotation.level());
      var result = joinPoint.proceed();
      log(logger, createMessage(annotation.after(), methodName), annotation.level());
      return result;
    } catch (Exception e) {
      logger.error(createMessage(annotation.onException(), methodName, e.getMessage()), e);
      if (annotation.rethrow())
        throw e;
      return null;
    }
  }

  private static Object exception(ProceedingJoinPoint joinPoint, String message, Logger logger, String methodName)
    throws Throwable {
    try {
      return joinPoint.proceed();
    } catch (Exception e) {
      logger.error(createMessage(message, methodName, e.getMessage()), e);
      return null;
    }
  }

  private static Object after(ProceedingJoinPoint joinPoint, String message, Logger logger, String methodName, Level level)
    throws Throwable {
    var result = joinPoint.proceed();
    log(logger, createMessage(message, methodName), level);
    return result;
  }

  private static Object before(ProceedingJoinPoint joinPoint, String message, Logger logger, String methodName, Level level)
    throws Throwable {
    log(logger, createMessage(message, methodName), level);
    return joinPoint.proceed();
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
