package com.compilit.validation.api;

import java.util.function.Function;
import org.slf4j.Logger;

final class VoidRuleValidatorBuilder<T> extends AbstractLoggingValidator<T> implements VoidValidationBuilder {

  private final Runnable runnable;

  VoidRuleValidatorBuilder(final Subject<T> subject, final Runnable runnable) {
    super(subject);
    this.runnable = runnable;
  }

  @Override
  public <E extends RuntimeException> Void orElseThrow(final Function<String, E> throwableFunction) {
    final var isValid = subject.validate();
    if (!isValid) {
      throw throwableFunction.apply(subject.getMessage());
    }
    runnable.run();
    return null;
  }

  @Override
  public boolean orElseLogMessage(final Logger logger) {
    final var isValid = subject.validate();
    if (isValid) {
      runnable.run();
    }
    return super.orElseLogMessage(logger);
  }
}
