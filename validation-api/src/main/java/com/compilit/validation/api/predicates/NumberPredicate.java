package com.compilit.validation.api.predicates;

import com.compilit.validation.api.ConstraintFinisher;
import java.util.function.Predicate;

public final class NumberPredicate {

  private NumberPredicate() {
  }

  /**
   * Checks whether the actual value is present.
   *
   * @return Predicate to continue adding rules.
   */
  public static Predicate<Integer> isNotNull() {
    return ObjectPredicate.isNotNull();
  }

  /**
   * Checks whether the actual value is not present.
   *
   * @return Predicate to continue adding rules.
   */
  public static Predicate<Integer> isNull() {
    return ObjectPredicate.isNull();
  }

  /**
   * Check if the actual value is equal to the given one.
   *
   * @param value the exact expected value.
   * @return Predicate to continue adding rules.
   */
  public static Predicate<Integer> isAnIntegerEqualTo(final int value) {
    return ObjectPredicate.isEqualTo(value);
  }

  /**
   * Check if the actual value is not equal to the given one.
   *
   * @param value the exact expected value.
   * @return Predicate to continue adding rules.
   */
  public static Predicate<Integer> isAnIntegerNotEqualTo(final int value) {
    return isAnIntegerEqualTo(value).negate();
  }

  /**
   * @param amountOfDigits the exact amount of digits of the Integer.
   * @return Predicate to continue adding rules.
   */
  public static Predicate<Integer> isAnIntegerWithAmountOfDigits(final int amountOfDigits) {
    return x -> String.valueOf(x).length() == amountOfDigits;
  }

  /**
   * @param first the first (inclusive) constraint. Can be either the high constraint or the low constraint.
   * @return a ChainingPredicate to add the second constraint.
   */
  public static ConstraintFinisher<Integer, Predicate<Integer>> isAnIntegerBetween(final int first) {
    return new NumberConstraintFinisher<>(first);
  }

  /**
   * Checks whether the given Integers are present anywhere in the value.
   *
   * @param value  the exact value that needs to be present in the toString of the original value.
   * @param values the optional exact values that needs to be present in the toString of the original value.
   * @return Predicate to continue adding rules.
   */
  public static Predicate<Integer> isAnIntegerContaining(final Integer value, final Integer... values) {
    return ObjectPredicate.contains(value, (Object[]) values);
  }

  /**
   * Checks whether the given Integers are present anywhere in the value.
   *
   * @param value  the exact value that needs to be present in the toString of the original value.
   * @param values the optional exact values that needs to be present in the toString of the original value.
   * @return Predicate to continue adding rules.
   */
  public static Predicate<Integer> isAnIntegerContainingOnly(final Integer value, final Integer... values) {
    return ObjectPredicate.containsOnly(value, (Object[]) values);
  }

  /**
   * Checks whether the given Integers are not present anywhere in the value.
   *
   * @param value  the exact value that may not be present in the toString of the original value.
   * @param values the optional exact values that may not be present in the toString of the original value.
   * @return Predicate to continue adding rules.
   */
  public static Predicate<Integer> isAnIntegerNotContaining(final Integer value, final Integer... values) {
    return ObjectPredicate.doesNotContain(value, (Object[]) values);
  }

}
