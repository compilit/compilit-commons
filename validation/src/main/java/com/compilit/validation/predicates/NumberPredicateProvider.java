package com.compilit.validation.predicates;

import java.util.function.Predicate;

public interface NumberPredicateProvider {


  /**
   * Check if the actual value is equal to the given one.
   *
   * @param value the exact expected value.
   * @return Predicate to continue adding rules.
   */
  default Predicate<Integer> isAnIntegerEqualTo(final int value) {
    return NumberPredicate.isAnIntegerEqualTo(value);
  }

  /**
   * Check if the actual value is not equal to the given one.
   *
   * @param value the exact expected value.
   * @return Predicate to continue adding rules.
   */
  default Predicate<Integer> isAnIntegerNotEqualTo(final int value) {
    return isAnIntegerEqualTo(value).negate();
  }

  /**
   * @param amountOfDigits the exact amount of digits of the Integer.
   * @return Predicate to continue adding rules.
   */
  default Predicate<Integer> isAnIntegerWithAmountOfDigits(final int amountOfDigits) {
    return NumberPredicate.isAnIntegerWithAmountOfDigits(amountOfDigits);
  }

  /**
   * @param first the first (inclusive) constraint. Can be either the high constraint or the low constraint.
   * @return a ChainingPredicate to add the second constraint.
   */
  default ConstraintAppender<Integer, Predicate<Integer>> isAnIntegerBetween(final int first) {
    return NumberPredicate.isAnIntegerBetween(first);
  }

  /**
   * Checks whether the given Integers are present anywhere in the value.
   *
   * @param value  the exact value that needs to be present in the toString of the original value.
   * @param values the optional exact values that needs to be present in the toString of the original value.
   * @return Predicate to continue adding rules.
   */
  default Predicate<Integer> isAnIntegerContaining(final Integer value, final Integer... values) {
    return NumberPredicate.isAnIntegerContaining(value, values);
  }

  /**
   * Checks whether only the given Integers are present anywhere in the value.
   *
   * @param value  the exact value that needs to be present in the toString of the original value.
   * @param values the optional exact values that needs to be present in the toString of the original value.
   * @return Predicate to continue adding rules.
   */
  default Predicate<Integer> isAnIntegerContainingOnly(final Integer value, final Integer... values) {
    return NumberPredicate.isAnIntegerContainingOnly(value, values);
  }

  /**
   * Checks whether the given Integers are not present anywhere in the value.
   *
   * @param value  the exact value that may not be present in the toString of the original value.
   * @param values the optional exact values that may not be present in the toString of the original value.
   * @return Predicate to continue adding rules.
   */
  default Predicate<Integer> isAnIntegerNotContaining(final Integer value, final Integer... values) {
    return NumberPredicate.isAnIntegerNotContaining(value, values);
  }
}
