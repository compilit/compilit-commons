package com.compilit.core.api.validation;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface Rule<T> extends Predicate<T> {

  /**
   * @return the message containing information about the validation. Default to 'Nothing to report'.
   */
  String getMessage();

  interface WithDualInput<T> extends BiPredicate<T, Object> {

    /**
     * @return the message containing information about the validation. Default to 'Nothing to report'.
     */
    String getMessage();
  }

}
