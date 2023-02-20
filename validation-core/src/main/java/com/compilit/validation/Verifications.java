package com.compilit.validation;

import com.compilit.validation.api.ValidatingRuleBuilder;
import com.compilit.validation.api.ValidationBuilder;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public final class Verifications {

  private Verifications() {
  }

  /**
   * This function can be used to quickly check a value against a set of predicates. It is mainly a convenience function
   * that is easier to read than regular predicates.
   *
   * @param <T> the type of the value.
   * @return PredicateProvider to choose a specific kind of Predicate.
   */
  public static <T> PredicateProvider<T> verifyThatIt() {
    return new PredicateProvider<>();
  }

  /**
   * Input the value you wish to test against certain rules or predicates. Follow the fluent API to write your
   * statement.
   *
   * @param value the value on which to apply the rules.
   * @param <T>   the type of the value.
   * @return a Validatable to add rules to.
   */
  public static <T> ValidationBuilder<T> verifyThat(final T value) {
    return new RuleValidationBuilder<>(value);
  }

  /**
   * Input the value and the predicate you wish to use to test this value. Follow the fluent API to write your
   * statement.
   *
   * @param value     the value on which to apply the rules.
   * @param predicate the predicate you wish to test against the value.
   * @param <T>       the type of the value.
   * @return a Validatable to add rules to.
   */
  public static <T> ValidatingRuleBuilder verifyThat(final T value, Predicate<T> predicate) {
    return new ValidatingRuleDefinitionBuilder<>(value, predicate);
  }

  /**
   * Input the value and the bipredicate you wish to use to test this value. Follow the fluent API to write your
   * statement.
   *
   * @param value       the value on which to apply the rules.
   * @param biPredicate the biPredicate you wish to test against the value.
   * @param argument    the other value you wish to use in the test against the value using the biPredicate.
   * @param <T>         the type of the value.
   * @return a Validatable to add rules to.
   */
  public static <T> ValidatingRuleBuilder verifyThat(final T value,
                                                     Object argument,
                                                     BiPredicate<T, Object> biPredicate) {
    return new ValidatingRuleDefinitionBuilder.WithDualInput<>(value, argument, biPredicate);
  }

}
