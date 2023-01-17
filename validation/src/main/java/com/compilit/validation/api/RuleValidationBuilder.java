package com.compilit.validation.api;

import com.compilit.core.api.validation.Rule;
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
