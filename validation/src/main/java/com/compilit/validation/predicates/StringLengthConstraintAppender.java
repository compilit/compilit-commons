package com.compilit.validation.predicates;

import java.util.function.Predicate;

final class StringLengthConstraintAppender
  implements ConstraintAppender<Integer, Predicate<String>> {

  private final int first;

  StringLengthConstraintAppender(final int first) {
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
