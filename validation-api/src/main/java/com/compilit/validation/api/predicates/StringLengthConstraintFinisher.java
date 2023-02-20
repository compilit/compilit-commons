package com.compilit.validation.api.predicates;

import com.compilit.validation.api.ConstraintFinisher;
import java.util.function.Predicate;

final class StringLengthConstraintFinisher
  implements ConstraintFinisher<Integer, Predicate<String>> {

  private final int first;

  StringLengthConstraintFinisher(final int first) {
    this.first = first;
  }

  @Override
  public Predicate<String> and(final Integer second) {
    if (second > first) {
      return x -> x.length() <= second && x.length() >= first;
    } else {
      return x -> x.length() <= first && x.length() >= second;
    }
  }

}
