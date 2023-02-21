package com.compilit.validation.api;

/**
 * Any object that needs to be validatable can implement this interface. The idea behind the getMessage method is to
 * provide an explanation as to why the validation as failed.
 */
public interface Validatable {

  /**
   * @return {@code true} if all rules pass. False if at least one rule fails.
   */
  boolean validate();

  /**
   * @return the message containing information about the validation. Default to 'Nothing to report'.
   */
  default String getMessage() {
    return Messages.DEFAULT_MESSAGE;
  }

}
