package com.compilit.validation.api;

public interface ConstraintFinisher<T, R> {

  /**
   * @param second the second of the (inclusive) constraints. Can be either the high constraint or the low constraint.
   * @return Predicate to continue adding rules.
   */
  R and(T second);
}
