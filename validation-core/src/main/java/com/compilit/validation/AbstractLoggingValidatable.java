package com.compilit.validation;

import com.compilit.validation.api.LoggingValidatable;
import com.compilit.validation.api.Validatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractLoggingValidatable<T> implements LoggingValidatable, Validatable {

  protected final Subject<T> subject;


  AbstractLoggingValidatable(Subject<T> subject) {
    this.subject = subject;
  }

  @Override
  public boolean orElseLogMessage() {
    var logger = LoggerFactory.getLogger(Verifications.class);
    return orElseLogMessage(logger);
  }

  @Override
  public boolean orElseLogMessage(final Logger logger) {
    final var isValid = subject.validate();
    if (!isValid) {
      logger.error(subject.getMessage());
    }
    if (isValid) {
      subject.processIntermediateActions();
    }
    return isValid;
  }

  @Override
  public boolean validate() {
    return subject.validate();
  }

  @Override
  public String getMessage() {
    return subject.getMessage();
  }
}
