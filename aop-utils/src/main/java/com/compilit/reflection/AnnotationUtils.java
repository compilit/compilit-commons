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

//  public static Object[] getArgumentsAnnotatedWith(ProceedingJoinPoint joinPoint,
//                                                    Class<? extends Annotation> annotation) {
//    MethodSignature methodSig = (MethodSignature) joinPoint.getSignature();
//    var parameters = methodSig.getMethod().getParameters();
//    List<Integer> argumentIndices = new ArrayList<>();
//    for (int index = 0; index < parameters.length; index++) {
//      if (parameters[index].isAnnotationPresent(annotation)) {
//        argumentIndices.add(index);
//      }
//    }
//    var methodArguments = joinPoint.getArgs();
//    var argumentsToDecrypt = new Object[argumentIndices.size()];
//    for (var index : argumentIndices) {
//      argumentsToDecrypt[index] = methodArguments[index];
//    }
//    return argumentsToDecrypt;
//  }

  public static Map<Object, ? extends Annotation> getArgumentsAnnotatedWith(ProceedingJoinPoint joinPoint,
                                                                  Class<? extends Annotation> annotation) {

    MethodSignature methodSig = (MethodSignature) joinPoint.getSignature();
    var parameters = methodSig.getMethod().getParameters();
    List<Integer> argumentIndices = new ArrayList<>();
    for (int index = 0; index < parameters.length; index++) {
      if (parameters[index].isAnnotationPresent(annotation)) {
        argumentIndices.add(index);
      }
    }
    var methodArguments = joinPoint.getArgs();
//    var arguments = new Object[argumentIndices.size()];
    var arguments = new HashMap<Object, Annotation>();
    for (var index : argumentIndices) {
      arguments.put(methodArguments[index], parameters[index].getAnnotation(annotation));
    }
    return arguments;
  }
}
