package com.compilit.validation.api;

final class DualInputArgumentAppender<T> extends AbstractLoggingValidator<T> implements ArgumentAppender<T> {

  DualInputArgumentAppender(final Subject<T> subject) {
    super(subject);
  }

  @Override
  public ContinuingValidationBuilder<T> whileApplying(final Object argument) {
    subject.setArgument(argument);
    return new ContinuingRuleValidationBuilder<>(subject);
  }
}
