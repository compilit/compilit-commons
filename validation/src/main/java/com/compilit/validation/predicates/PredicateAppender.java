package com.compilit.validation.predicates;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface PredicateAppender<T> {

  /**
   * @param predicate the custom predicate to test against properties of T.
   * @return Predicate to continue adding rules.
   */
  default Predicate<T> where(final Predicate<T> predicate) {
    return predicate;
  }

  /**
   * @param predicate the custom predicate to test against properties of T.
   * @return Predicate to continue adding rules.
   */
  default BiPredicate<T, Object> where(final BiPredicate<T, Object> predicate) {
    return predicate;
  }

  /**
   * @param predicate the custom predicate to test against properties of T.
   * @return Predicate to continue adding rules.
   */
  default Predicate<T> that(final Predicate<T> predicate) {
    return predicate;
  }

  /**
   * @param predicate the custom predicate to test against properties of T.
   * @return Predicate to continue adding rules.
   */
  default BiPredicate<T, Object> that(final BiPredicate<T, Object> predicate) {
    return predicate;
  }

}
