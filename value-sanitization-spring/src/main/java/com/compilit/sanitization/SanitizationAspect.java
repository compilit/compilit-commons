package com.compilit.sanitization;

import com.compilit.sanitization.api.Sanitized;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
class SanitizationAspect {

  private final static Logger logger = LoggerFactory.getLogger(SanitizationAspect.class);

  @Around("@annotation(annotation)")
  public Object sanitizeArguments(ProceedingJoinPoint joinPoint, Sanitized annotation) throws Throwable {
    var argumentsToSanitize = joinPoint.getArgs();
    for (var argumentToSanitize : argumentsToSanitize) {
      if (argumentToSanitize instanceof CharSequence asString) {
        var sanitizedValue = Sanitizers.sanitize(asString);
        var arguments = joinPoint.getArgs();
        var alteredArguments = modifyArguments(arguments, sanitizedValue);
        return joinPoint.proceed(alteredArguments);
      }
    }
    logger.warn("Method annotated with @Sanitized does not contain any String arguments");
    return joinPoint.proceed();
  }

  private static Object[] modifyArguments(Object[] arguments, Object alteredValue) {
    var alteredArguments = new Object[arguments.length];
    var indexOfValueToAlter = 0;
    for (int index = 0; index < alteredArguments.length; index++) {
      if (arguments[index].equals(alteredValue)) {
        indexOfValueToAlter = index;
      } else {
        alteredArguments[index] = arguments[index];
      }
    }
    alteredArguments[indexOfValueToAlter] = alteredValue;
    return alteredArguments;
  }

}
