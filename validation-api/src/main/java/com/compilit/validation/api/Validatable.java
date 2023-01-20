package com.compilit.validation.api;

public interface Validatable {

  /**
   * @return {@code true} if all rules pass. False if at least one rule fails.
   */
  boolean validate();

  /**
   * @return the message containing information about the validation. Default to 'Nothing to report'.
   */
  String getMessage();

}
