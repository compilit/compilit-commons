package com.compilit.validation;

import com.compilit.validation.api.ArgumentAdder;
import com.compilit.validation.api.ContinuingValidationBuilder;

final class DualInputArgumentAdder<T> extends AbstractLoggingValidatable<T> implements ArgumentAdder<T> {

  DualInputArgumentAdder(final Subject<T> subject) {
    super(subject);
  }

  @Override
  public ContinuingValidationBuilder<T> whileApplying(final Object argument) {
    subject.setArgument(argument);
    return new ContinuingRuleValidationBuilder<>(subject);
  }
}
