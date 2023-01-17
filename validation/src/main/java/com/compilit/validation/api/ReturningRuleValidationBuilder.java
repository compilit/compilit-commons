package com.compilit.validation.api;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

final class ReturningRuleValidationBuilder<T, R> extends AbstractLoggingValidator<T>
  implements ReturningValidationBuilder<R> {

  private final Supplier<R> supplier;

  ReturningRuleValidationBuilder(final Subject<T> subject, final Supplier<R> supplier) {
    super(subject);
    this.supplier = supplier;
  }

  ReturningRuleValidationBuilder(final Subject<T> subject, final Consumer<T> consumer) {
    super(subject);
    this.supplier = () -> {
      consumer.accept(subject.getValue());
      return null;
    };
  }

  ReturningRuleValidationBuilder(final Subject<T> subject, final Function<T, R> function) {
    super(subject);
    this.supplier = () -> function.apply(subject.getValue());
  }

  @Override
  public <E extends RuntimeException> R orElseThrow(final Function<String, E> throwableFunction) {
    final var isValid = subject.validate();
    if (!isValid) {
      throw throwableFunction.apply(subject.getMessage());
    }
    subject.processIntermediateActions();
    return supplier.get();
  }

  @Override
  public R orElseReturn(final R other) {
    final var isValid = subject.validate();
    if (!isValid) {
      return other;
    }
    subject.processIntermediateActions();
    return supplier.get();
  }

  @Override
  public R orElseReturn(final Function<String, R> other) {
    final var isValid = subject.validate();
    final var message = subject.getMessage();
    if (!isValid) {
      return other.apply(message);
    }
    subject.processIntermediateActions();
    return supplier.get();
  }

}
