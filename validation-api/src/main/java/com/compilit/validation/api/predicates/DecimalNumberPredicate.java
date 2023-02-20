package com.compilit.validation.api.predicates;

import com.compilit.validation.api.ConstraintFinisher;
import java.util.function.Predicate;

public final class DecimalNumberPredicate {

  private DecimalNumberPredicate() {
  }

  /**
   * Checks whether the actual value is present.*
   *
   * @return Predicate to continue adding rules.
   */
  public static Predicate<Double> isNotNull() {
    return ObjectPredicate.isNotNull();
  }

  /**
   * Checks whether the actual value is not present.
   *
   * @return Predicate to continue adding rules.
   */
  public static Predicate<Double> isNull() {
    return ObjectPredicate.isNull();
  }

  /**
   * @param first the first (inclusive) constraint. Can be either the high constraint or the low constraint.
   * @return a ChainingPredicate to add the second constraint.
   */
  public static ConstraintFinisher<Double, Predicate<Double>> isADecimalNumberBetween(final double first) {
    return new NumberConstraintFinisher<>(first);
  }

  /**
   * Check if the actual value is equal to the given one.
   *
   * @param value the exact expected value.
   * @return Predicate to continue adding rules.
   */
  public static Predicate<Double> isADecimalNumberEqualTo(final double value) {
    return ObjectPredicate.isEqualTo(value);
  }

  /**
   * Check if the actual value is equal to the given one.
   *
   * @param value the exact expected value.
   * @return Predicate to continue adding rules.
   */
  public static Predicate<Double> isADecimalNumberNotEqualTo(final double value) {
    return isADecimalNumberEqualTo(value).negate();
  }

  /**
   * Checks whether the given Integers are present anywhere in the value.
   *
   * @param value  the exact value that needs to be present in the toString of the original value.
   * @param values the optional exact values that needs to be present in the toString of the original value.
   * @return Predicate to continue adding rules.
   */
  public static Predicate<Double> isADecimalNumberContaining(final Integer value, final Integer... values) {
    return ObjectPredicate.contains(value, (Object[]) values);
  }

  /**
   * Checks whether only the given Integers are present anywhere in the value.
   *
   * @param value  the exact value that needs to be present in the toString of the original value.
   * @param values the optional exact values that needs to be present in the toString of the original value.
   * @return Predicate to continue adding rules.
   */
  public static Predicate<Double> isADecimalNumberContainingOnly(final Integer value, final Integer... values) {
    return ObjectPredicate.containsOnly(value, (Object[]) values);
  }

  /**
   * Checks whether the given Integers are not present anywhere in the value.
   *
   * @param value  the exact value that may not be present in the toString of the original value.
   * @param values the optional exact values that may not be present in the toString of the original value.
   * @return Predicate to continue adding rules.
   */
  public static Predicate<Double> isADecimalNumberNotContaining(final Integer value, final Integer... values) {
    return ObjectPredicate.doesNotContain(value, (Object[]) values);
  }

}
