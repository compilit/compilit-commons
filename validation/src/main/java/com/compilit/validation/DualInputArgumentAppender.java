package com.compilit.validation;

import com.compilit.validation.api.ArgumentAppender;
import com.compilit.validation.api.ContinuingValidationBuilder;

final class DualInputArgumentAppender<T> extends AbstractLoggingValidatable<T> implements ArgumentAppender<T> {

  DualInputArgumentAppender(final Subject<T> subject) {
    super(subject);
  }

  @Override
  public ContinuingValidationBuilder<T> whileApplying(final Object argument) {
    subject.setArgument(argument);
    return new ContinuingRuleValidationBuilder<>(subject);
  }
}
