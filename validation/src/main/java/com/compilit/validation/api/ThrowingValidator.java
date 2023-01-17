package com.compilit.validation.api;

import java.util.function.Function;

public interface ThrowingValidator<R> {

  /**
   * @param throwableFunction the function defining the Exception that needs to be thrown when a rule is broken. The
   *                          String is the stored failure message of the validation.
   * @param <E>               the bound of the Exception that needs to be thrown when a rule is broken.
   * @return R if all rules pass.
   */
  <E extends RuntimeException> R orElseThrow(Function<String, E> throwableFunction);

}
