package com.compilit.validation;

import com.compilit.validation.api.VoidValidationBuilder;
import java.util.function.Function;
import org.slf4j.Logger;

final class VoidRuleValidatableBuilder<T> extends AbstractLoggingValidatable<T> implements VoidValidationBuilder {

  private final Runnable runnable;

  VoidRuleValidatableBuilder(final Subject<T> subject, final Runnable runnable) {
    super(subject);
    this.runnable = runnable;
  }

  @Override
  public boolean orElseLogMessage(final Logger logger) {
    final var isValid = subject.validate();
    if (isValid) {
      runnable.run();
    }
    return super.orElseLogMessage(logger);
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
}
