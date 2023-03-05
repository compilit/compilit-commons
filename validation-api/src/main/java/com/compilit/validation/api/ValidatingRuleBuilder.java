package com.compilit.validation.api;

/**
 * Part if the Fluent API to create a "validation", which is used to validate an object against certain predefined
 * Rules. This is the ending part of the API, after which the validation takes place.
 */
public interface ValidatingRuleBuilder {

  /**
   * @param message         the message you wish to add to the pipe in case of a failed validation.
   * @param formatArguments the extra arguments you wish to place on the standardized format '%s' placeholders.
   * @return the finished Rule, ready to validate.
   */
  boolean otherwiseReport(String message, Object... formatArguments);

}
