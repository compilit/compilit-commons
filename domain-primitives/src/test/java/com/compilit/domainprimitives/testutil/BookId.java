package com.compilit.domainprimitives.testutil;

import static com.compilit.validation.Definitions.defineThatIt;
import static com.compilit.validation.api.predicates.ObjectPredicate.isA;
import static com.compilit.validation.api.predicates.StringPredicate.isNotNullEmptyOrBlank;

import com.compilit.validation.api.Rule;
import com.compilit.domainprimitives.DomainPrimitive;
import java.util.function.Predicate;

public class BookId extends DomainPrimitive<String> {

  private static final Rule<String> nullRule = defineThatIt(isA(String.class).that(isNotNullEmptyOrBlank()))
    .otherwiseReport("Book id cannot be null");
  private static final Rule<String> digitsRule = defineThatIt(isA(String.class).that(containsAValidDigitPart()))
    .otherwiseReport("Value does not contain 10 digits");

  private static final Rule<String> lettersRule = defineThatIt(isA(String.class).that(containsValidLettersPart()))
    .otherwiseReport("Value does not contain 5 letters");

  public BookId(String value) {
    super(value, BookId.class.getSimpleName(), nullRule, digitsRule, lettersRule);
  }

  private static Predicate<String> containsAValidDigitPart() {
    return value -> {
      var substrings = value.split(":");
      var digits = substrings[0];
      return digits.length() == 10 &&
        digits.chars().allMatch(Character::isDigit);
    };
  }

  private static Predicate<String> containsValidLettersPart() {
    return value -> {
      var substrings = value.split(":");
      var letters = substrings[1];
      return letters.length() == 5 &&
        letters.chars().allMatch(Character::isAlphabetic);
    };
  }
}
