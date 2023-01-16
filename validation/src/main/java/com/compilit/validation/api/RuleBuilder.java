package com.compilit.validation.api;

import com.compilit.core.api.validation.Rule;

public interface RuleBuilder<T> {

  /**
   * @param message         the message you wish to add to the pipe in case of a failed validation.
   * @param formatArguments the extra arguments you wish to place on the standardized format '%s' placeholders.
   * @return the finished Rule, ready to validate.
   */
  Rule<T> otherwiseReport(String message, Object... formatArguments);

  interface WithDualInput<T> {

    /**
     * @param message         the message you wish to add to the pipe in case of a failed validation.
     * @param formatArguments the extra arguments you wish to place on the standardized format '%s' placeholders.
     * @return the finished Rule, ready to validate.
     */
    Rule.WithDualInput<T> otherwiseReport(String message, Object... formatArguments);
  }
}
