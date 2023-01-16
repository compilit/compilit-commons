package com.compilit.validation.predicates;

import java.util.function.Predicate;

public interface DecimalNumberPredicateProvider {

  /**
   * @param first the first (inclusive) constraint. Can be either the high constraint or the low
   *              constraint.
   * @return a ChainingPredicate to add the second constraint.
   */
  default ConstraintAppender<Double, Predicate<Double>> isADecimalNumberBetween(final double first) {
    return DecimalNumberPredicate.isADecimalNumberBetween(first);
  }

  /**
   * Check if the actual value is equal to the given one.
   *
   * @param value the exact expected value.
   * @return Predicate to continue adding rules.
   */
  default Predicate<Double> isADecimalNumberEqualTo(final double value) {
    return DecimalNumberPredicate.isADecimalNumberEqualTo(value);
  }

  /**
   * Check if the actual value is equal to the given one.
   *
   * @param value the exact expected value.
   * @return Predicate to continue adding rules.
   */
  default Predicate<Double> isADecimalNumberNotEqualTo(final double value) {
    return isADecimalNumberEqualTo(value).negate();
  }

  /**
   * Checks whether the given Integers are present anywhere in the value.
   *
   * @param value  the exact value that needs to be present in the toString of the original value.
   * @param values the optional exact values that needs to be present in the toString of the
   *               original value.
   * @return Predicate to continue adding rules.
   */
  default Predicate<Double> isADecimalNumberContaining(final Integer value, final Integer... values) {
    return DecimalNumberPredicate.isADecimalNumberContaining(value, values);
  }

  /**
   * Checks whether the given Integers are not present anywhere in the value.
   *
   * @param value  the exact value that may not be present in the toString of the original value.
   * @param values the optional exact values that may not be present in the toString of the original
   *               value.
   * @return Predicate to continue adding rules.
   */
  default Predicate<Double> isADecimalNumberNotContaining(final Integer value, final Integer... values) {
    return DecimalNumberPredicate.isADecimalNumberNotContaining(value, values);
  }

}
