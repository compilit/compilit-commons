package com.compilit.reflection;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public final class AnnotationUtils {

  private AnnotationUtils() {}

  public static <T extends Annotation> Map<Object, T> getArgumentsAnnotatedWith(ProceedingJoinPoint joinPoint,
                                                                                Class<T> annotation) {
    var methodSig = (MethodSignature) joinPoint.getSignature();
    var parameters = methodSig.getMethod().getParameters();
    var argumentIndices = new ArrayList<Integer>();
    for (int index = 0; index < parameters.length; index++) {
      if (parameters[index].isAnnotationPresent(annotation)) {
        argumentIndices.add(index);
      }
    }
    var methodArguments = joinPoint.getArgs();
    var arguments = new HashMap<Object, T>();
    for (var index : argumentIndices) {
      arguments.put(methodArguments[index], parameters[index].getAnnotation(annotation));
    }
    return arguments;
  }
}
