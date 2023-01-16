package com.compilit.validation.api;

public interface ValidatingRuleBuilder {

  /**
   * @param message         the message you wish to add to the pipe in case of a failed validation.
   * @param formatArguments the extra arguments you wish to place on the standardized format '%s' placeholders.
   * @return the finished Rule, ready to validate.
   */
  boolean otherwiseReport(String message, Object... formatArguments);

}
