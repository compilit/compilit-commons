package com.compilit.validation;

import com.compilit.validation.api.ActionValidationBuilder;
import com.compilit.validation.api.ContinuingValidationBuilder;
import com.compilit.validation.api.ReturningValidationBuilder;
import com.compilit.validation.api.Rule;
import com.compilit.validation.api.VoidValidationBuilder;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

final class ContinuingRuleValidationBuilder<T> extends AbstractLoggingValidatable<T> implements
  ContinuingValidationBuilder<T> {

  ContinuingRuleValidationBuilder(final Subject<T> subject) {
    super(subject);
  }

  @Override
  public ContinuingValidationBuilder<T> and(final Rule<T> rule) {
    subject.addRule(rule);
    return this;
  }

  @Override
  public ContinuingValidationBuilder<T> and(Rule.WithDualInput<T> rule) {
    subject.addDualInputRule(rule);
    return this;
  }

  @Override
  public ActionValidationBuilder<T> thenApply(final Consumer<T> consumer) {
    subject.addIntermediateAction(consumer);
    return this;
  }

  @Override
  public ReturningValidationBuilder<T> andThen(final Consumer<T> consumer) {
    return new ReturningRuleValidationBuilder<>(subject, consumer);
  }

  @Override
  public <R> ReturningValidationBuilder<R> andThen(final Supplier<R> supplier) {
    return new ReturningRuleValidationBuilder<>(subject, supplier);
  }

  @Override
  public VoidValidationBuilder andThen(final Runnable runnable) {
    return new VoidRuleValidatableBuilder<>(subject, runnable);
  }

  @Override
  public <R> ReturningValidationBuilder<R> andThen(final Function<T, R> function) {
    return new ReturningRuleValidationBuilder<>(subject, function);
  }

  @Override
  public <E extends RuntimeException> T orElseThrow(final Function<String, E> throwableFunction) {
    final var isValid = subject.validate();
    if (!isValid) {
      throw throwableFunction.apply(subject.getMessage());
    }
    subject.processIntermediateActions();
    return subject.getValue();
  }

  @Override
  public T orElseReturn(final T other) {
    final var isValid = subject.validate();
    if (!isValid) {
      return other;
    }
    subject.processIntermediateActions();
    return subject.getValue();
  }

  @Override
  public T orElseReturn(final Function<String, T> other) {
    final var isValid = subject.validate();
    if (!isValid) {
      return other.apply(subject.getMessage());
    }
    subject.processIntermediateActions();
    return subject.getValue();
  }

}
