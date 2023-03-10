package com.compilit.validation.api.predicates;

import static com.compilit.validation.Definitions.defineThatIt;
import static com.compilit.validation.Verifications.verifyThat;
import static com.compilit.validation.api.predicates.NumberPredicate.isAnIntegerBetween;
import static com.compilit.validation.api.predicates.NumberPredicate.isAnIntegerContaining;
import static com.compilit.validation.api.predicates.NumberPredicate.isAnIntegerEqualTo;
import static com.compilit.validation.api.predicates.NumberPredicate.isAnIntegerNotContaining;
import static com.compilit.validation.api.predicates.NumberPredicate.isAnIntegerNotEqualTo;
import static com.compilit.validation.api.predicates.NumberPredicate.isAnIntegerWithAmountOfDigits;
import static com.compilit.validation.api.predicates.NumberPredicate.isNotNull;
import static com.compilit.validation.api.predicates.NumberPredicate.isNull;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.compilit.validation.api.Rule;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class NumberPredicateBuilderTests {

  private static Stream<Arguments> integerTestCases() {
    return Stream.of(
      arguments(defineThatIt(isAnIntegerEqualTo(2)).otherwiseReport("failure")),
      arguments(defineThatIt(isAnIntegerBetween(1).and(5)).otherwiseReport("failure")),
      arguments(defineThatIt(isAnIntegerBetween(5).and(1)).otherwiseReport("failure")),
      arguments(defineThatIt(isAnIntegerWithAmountOfDigits(1)).otherwiseReport("failure")),
      arguments(defineThatIt(isAnIntegerContaining(2, 2)).otherwiseReport("failure")),
      arguments(defineThatIt(isAnIntegerNotContaining(43, 123)).otherwiseReport("failure")),
      arguments(defineThatIt(isNotNull()).otherwiseReport("failure")),
      arguments(defineThatIt(isAnIntegerEqualTo(2)
                               .and(isAnIntegerEqualTo(2)
                                      .and(isAnIntegerEqualTo(2)))).otherwiseReport("failure")),
      arguments(defineThatIt(isAnIntegerNotEqualTo(1)).otherwiseReport("failure")),
      arguments(defineThatIt(isAnIntegerWithAmountOfDigits(1)).otherwiseReport("failure"))
    );
  }

  @ParameterizedTest
  @MethodSource("integerTestCases")
  void validate_integerMatchingRule_shouldReturnTrue(Rule<Integer> rule) {
    var value = 2;
    Assertions.assertThat(verifyThat(value).compliesWith(rule).validate()).isTrue();
  }

  private static Stream<Arguments> integerInvalidTestCases() {
    return Stream.of(
      arguments(defineThatIt(isAnIntegerEqualTo(1)).otherwiseReport("failure")),
      arguments(defineThatIt(isAnIntegerBetween(5).and(50)).otherwiseReport("failure")),
      arguments(defineThatIt(isAnIntegerBetween(50).and(5)).otherwiseReport("failure")),
      arguments(defineThatIt(isAnIntegerWithAmountOfDigits(10)).otherwiseReport("failure")),
      arguments(defineThatIt(isAnIntegerEqualTo(5435)).otherwiseReport("failure")),
      arguments(defineThatIt(isAnIntegerNotContaining(2, 2)).otherwiseReport("failure")),
      arguments(defineThatIt(isNull()).otherwiseReport("failure")),
      arguments(
        defineThatIt(isAnIntegerEqualTo(2)
                                 .and(isAnIntegerEqualTo(2)
                                        .and(isAnIntegerEqualTo(1)))).otherwiseReport("failure")
      ),
      arguments(defineThatIt(isAnIntegerNotEqualTo(2)).otherwiseReport("failure")),
      arguments(defineThatIt(isAnIntegerWithAmountOfDigits(10)).otherwiseReport("failure"))
    );
  }


  @ParameterizedTest
  @MethodSource("integerInvalidTestCases")
  void validate_integerNotMatchingRule_shouldReturnFalse(Rule<Integer> rule) {
    var value = 2;
    Assertions.assertThat(verifyThat(value).compliesWith(rule).validate()).isFalse();
  }

  @Test
  void validate_dualInputTests() {
    var value = 123456789;
    var rule1 = defineThatIt(isAnIntegerContaining(1, 2, 3, 4, 5, 6, 7, 8, 9, 12, 23, 789)).otherwiseReport("failure");
    var rule2 = defineThatIt(isAnIntegerContaining(
      10,
      11,
      22,
      33,
      44,
      55,
      66,
      77,
      88,
      9999
    )).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(value).compliesWith(rule1).validate()).isTrue();
    Assertions.assertThat(verifyThat(value).compliesWith(rule2).validate()).isFalse();
  }
}
