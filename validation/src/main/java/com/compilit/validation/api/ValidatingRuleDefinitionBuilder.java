package com.compilit.validation.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

class ValidatingRuleDefinitionBuilder<T> implements ValidatingRuleBuilder {

  private final T value;
  private final Predicate<T> predicate;
  private final Logger logger = LoggerFactory.getLogger(Verifications.class);

  ValidatingRuleDefinitionBuilder(final T value, final Predicate<T> predicate) {
    this.value = value;
    this.predicate = predicate;
  }

  @Override
  public boolean otherwiseReport(final String message, final Object... formatArguments) {
    final var actualMessage = String.format(message, formatArguments);
    var isValid = predicate.test(value);
    if (!isValid) {
      logger.error(actualMessage);
    }
    return isValid;
  }

  static final class WithDualInput<T> implements ValidatingRuleBuilder {

    private final T value;
    private final Object argument;
    private final BiPredicate<T, Object> predicate;
    private final Logger logger = LoggerFactory.getLogger(Verifications.class);

    WithDualInput(final T value, Object argument, final BiPredicate<T, Object> predicate) {
      this.value = value;
      this.argument = argument;
      this.predicate = predicate;
    }

    @Override
    public boolean otherwiseReport(final String message, final Object... formatArguments) {
      final var actualMessage = String.format(message, formatArguments);
      var isValid = predicate.test(value, argument);
      if (!isValid) {
        logger.error(actualMessage);
      }
      return isValid;
    }

  }

}
