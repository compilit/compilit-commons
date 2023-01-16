package com.compilit.validation.api;

import com.compilit.core.api.validation.Rule;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

final class RuleDefinitionBuilder<T> implements RuleBuilder<T> {

  private final Predicate<T> predicate;

  RuleDefinitionBuilder(final Predicate<T> predicate) {
    this.predicate = predicate;
  }

  @Override
  public Rule<T> otherwiseReport(final String message, final Object... formatArguments) {
    final var actualMessage = String.format(message, formatArguments);
    return new RuleDefinition<>(predicate, actualMessage);
  }

  static final class WithDualInput<T> implements RuleBuilder.WithDualInput<T> {

    private final BiPredicate<T, Object> predicate;

    WithDualInput(final BiPredicate<T, Object> predicate) {
      this.predicate = predicate;
    }

    @Override
    public Rule.WithDualInput<T> otherwiseReport(final String message, final Object... formatArguments) {
      final var actualMessage = String.format(message, formatArguments);
      return new RuleDefinition.WithDualInput<>(predicate, actualMessage);
    }

  }

}
