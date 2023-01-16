package com.compilit.validation.api;

import org.slf4j.Logger;

public interface LoggingValidator {

  /**
   * Uses SLF4J API to log the internal message at Error level. Using the Validations class as its logger source.
   *
   * @return a boolean value. True if the validation was successful, false if it failed.
   */
  boolean orElseLogMessage();

  /**
   * Uses SLF4J API to log the internal message at Error level. Using the Validations class as its logger source.
   *
   * @param logger the logger used to log the message.
   * @return a boolean value. True if the validation was successful, false if it failed.
   */
  boolean orElseLogMessage(Logger logger);

}
