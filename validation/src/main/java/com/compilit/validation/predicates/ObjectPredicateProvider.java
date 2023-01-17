package com.compilit.validation.predicates;

import java.util.function.Predicate;

public interface ObjectPredicateProvider<T> {

  /**
   * Checks whether the actual value is present.*
   *
   * @return Predicate to continue adding rules.
   */
  default Predicate<T> isNotNull() {
    return ObjectPredicate.isNotNull();
  }

  /**
   * Checks whether the actual value is not present.
   *
   * @return Predicate to continue adding rules.
   */
  default Predicate<T> isNull() {
    return ObjectPredicate.isNull();
  }

  /**
   * @param clazz the class of the object to validate. This is only a compiler flag to be able to treat T as its
   *              instance.
   * @param <T>   the type upon which the validations are tested.
   * @return GenericPredicateRule to continue adding rules.
   */
  default <T> PredicateAppender<T> isA(final Class<T> clazz) {
    return ObjectPredicate.isA(clazz);
  }

  /**
   * @param clazz the class of the object to validate. This is only a compiler flag to be able to treat T as its
   *              instance.
   * @param <T>   the type upon which the validations are tested.
   * @return GenericPredicateRule to continue adding rules.
   */
  default <T> PredicateAppender<T> isAn(final Class<T> clazz) {
    return isA(clazz);
  }

  /**
   * Check if the actual value is equal to the given one.
   *
   * @param value the exact expected value.
   * @param <T>   the value which you wish to test against the original value.
   * @return Predicate to continue adding rules.
   */
  default <T> Predicate<T> isEqualTo(T value) {
    return ObjectPredicate.isEqualTo(value);
  }

  /**
   * Check if the actual value is not equal to the given one.
   *
   * @param value the exact expected value.
   * @return Predicate to continue adding rules.
   */
  default Predicate<T> isNotEqualTo(T value) {
    return isEqualTo(value).negate();
  }

  default Predicate<T> contains(Object value, Object... values) {
    return ObjectPredicate.contains(value, values);
  }

  default Predicate<T> containsOnly(Object value, Object... values) {
    return ObjectPredicate.containsOnly(value, values);
  }

  default Predicate<T> doesNotContain(Object value, Object... values) {
    return ObjectPredicate.doesNotContain(value, values);
  }
}
