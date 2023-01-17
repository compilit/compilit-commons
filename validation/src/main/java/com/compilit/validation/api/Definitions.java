package com.compilit.validation.api;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public final class Definitions {

  private Definitions() {
  }

  /**
   * Entry point for defining rules.
   *
   * @param predicate the predicate you wish to use for testing your value.
   * @param <T>       the type of the value.
   * @return a RuleBuilder to continue embellishing your rule.
   */
  public static <T> RuleBuilder<T> defineThatIt(Predicate<T> predicate) {
    return new RuleDefinitionBuilder<>(predicate);
  }

  /**
   * Entry point for defining rules with dual input.
   *
   * @param biPredicate the biPredicate you wish to use for testing your value.
   * @param <T>         the type of the value.
   * @return a RuleBuilder to continue embellishing your rule.
   */
  public static <T> RuleBuilder.WithDualInput<T> defineThatIt(BiPredicate<T, Object> biPredicate) {
    return new RuleDefinitionBuilder.WithDualInput<>(biPredicate);
  }

}
