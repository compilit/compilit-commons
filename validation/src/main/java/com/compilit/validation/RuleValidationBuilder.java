package com.compilit.validation;

import com.compilit.validation.api.ArgumentAppender;
import com.compilit.validation.api.ContinuingValidationBuilder;
import com.compilit.validation.api.Rule;
import com.compilit.validation.api.ValidationBuilder;
import java.util.Collection;

final class RuleValidationBuilder<T> implements ValidationBuilder<T> {

  private final T value;

  RuleValidationBuilder(final T value) {
    this.value = value;
  }

  @Override
  public ContinuingValidationBuilder<T> compliesWith(final Rule<T> rule) {
    var subject = new Subject<>(rule, value);
    return new ContinuingRuleValidationBuilder<>(subject);
  }

  @Override
  public ContinuingValidationBuilder<T> compliesWith(final Collection<Rule<T>> rules) {
    var subject = new Subject<T>(rules, value);
    return new ContinuingRuleValidationBuilder<>(subject);
  }

  @Override
  public ArgumentAppender<T> compliesWith(final Rule.WithDualInput<T> rule) {
    var subject = new Subject<T>(rule, value);
    return new DualInputArgumentAppender<>(subject);
  }

}
