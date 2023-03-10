package com.compilit.cryptography;

import static com.compilit.reflection.AnnotationUtils.getArgumentsAnnotatedWith;

import com.compilit.cryptography.api.Cryptographer;
import com.compilit.cryptography.api.Decrypt;
import com.compilit.cryptography.api.Decryptable;
import com.compilit.cryptography.api.Encrypt;
import com.compilit.cryptography.api.Encryptable;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
class CryptographyAspect {

  private final Cryptographer cryptographer;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  CryptographyAspect(Cryptographer cryptographer) {
    this.cryptographer = cryptographer;
  }

  @Around("@annotation(annotation)")
  public Object encryptArguments(ProceedingJoinPoint joinPoint, Encryptable annotation) throws Throwable {
    var argumentsToEncrypt = getArgumentsAnnotatedWith(joinPoint, Encrypt.class);
    for (var valueToEncrypt : argumentsToEncrypt.keySet()) {
      if (valueToEncrypt instanceof Serializable serializable) {
        var encryptedValue = cryptographer.encrypt(serializable);
        var arguments = joinPoint.getArgs();
        var alteredArguments = modifyArguments(arguments, encryptedValue);
        return joinPoint.proceed(alteredArguments);
      } else {
        logger.warn("Unable to encrypt value. Value has to be serializable");
      }
    }
    return joinPoint.proceed();
  }

  @Around("@annotation(annotation)")
  public Object decryptArguments(ProceedingJoinPoint joinPoint, Decryptable annotation) throws Throwable {
    var argumentsToDecrypt = getArgumentsAnnotatedWith(joinPoint, Decrypt.class);
    for (var valueToDecrypt : argumentsToDecrypt.keySet()) {
      if (valueToDecrypt instanceof String encryptedString) {
        return applyDecryption(joinPoint, encryptedString.getBytes());
      } else if (valueToDecrypt instanceof byte[] encryptedBytes) {
        return applyDecryption(joinPoint, encryptedBytes);
      } else {
        logger.warn("Unable to decrypt value. Value has to be a String or byte-array");
      }
    }
    return joinPoint.proceed();
  }

  private Object applyDecryption(ProceedingJoinPoint joinPoint, byte[] encryptedBytes) throws Throwable {
    var decryptedValue = cryptographer.decrypt(encryptedBytes);
    var arguments = joinPoint.getArgs();
    var alteredArguments = modifyArguments(arguments, decryptedValue);
    return joinPoint.proceed(alteredArguments);
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
