package com.compilit.validation.predicates;

public interface ConstraintAppender<T, R> {

  /**
   * @param second the second of the (inclusive) constraints. Can be either the high constraint or the low constraint.
   * @return Predicate to continue adding rules.
   */
  R and(T second);
}
