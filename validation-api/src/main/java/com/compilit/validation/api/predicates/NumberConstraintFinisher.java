package com.compilit.validation.api.predicates;

import com.compilit.validation.api.ConstraintFinisher;
import java.util.function.Predicate;

final class NumberConstraintFinisher<T extends Number>
  implements ConstraintFinisher<T, Predicate<T>> {

  private final T first;

  NumberConstraintFinisher(final T first) {
    this.first = first;
  }

  /**
   * @param second second of the constraints. Can be either the high constraint or the low constraint.
   * @return Predicate to continue adding rules.
   */
  @Override
  public Predicate<T> and(final T second) {
    if (second.doubleValue() > first.doubleValue()) {
      return x -> x.doubleValue() <= second.doubleValue() && x.doubleValue() >= first.doubleValue();
    } else {
      return x -> x.doubleValue() <= first.doubleValue() && x.doubleValue() >= second.doubleValue();
    }
  }

}
