package com.compilit.validation.api.predicates;

import static com.compilit.validation.Definitions.defineThatIt;
import static com.compilit.validation.api.predicates.DecimalNumberPredicate.*;

import com.compilit.validation.Verifications;
import com.compilit.validation.api.Rule;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DecimalNumberPredicateBuilderTests {


  public static Stream<Arguments> validTestCases() {
    return Stream.of(
      Arguments.arguments(defineThatIt(isADecimalNumberEqualTo(0.2)).otherwiseReport("I am error"), .2),
      Arguments.arguments(defineThatIt(isADecimalNumberBetween(.0).and(.5)).otherwiseReport("I am error"), .2),
      Arguments.arguments(defineThatIt(isADecimalNumberBetween(.5).and(.0)).otherwiseReport("I am error"), .2),
      Arguments.arguments(defineThatIt(isADecimalNumberContaining(0, 2)).otherwiseReport("I am error"), .2),
      Arguments.arguments(defineThatIt(isADecimalNumberContaining(2)).otherwiseReport("I am error"), .2),
      Arguments.arguments(defineThatIt(isADecimalNumberNotContaining(4, 5643)).otherwiseReport("I am error"), .2),
      Arguments.arguments(defineThatIt(isNotNull()).otherwiseReport("I am error"), .2),
      Arguments.arguments(defineThatIt(isADecimalNumberNotEqualTo(0.1)).otherwiseReport("I am error"), .2),
      Arguments.arguments(defineThatIt(isNull()).otherwiseReport("I am error"), null),
      Arguments.arguments(defineThatIt(isADecimalNumberContainingOnly(2, 0)).otherwiseReport("I am error"), .2)
    );
  }

  public static Stream<Arguments> invalidTestCases() {
    return Stream.of(
      Arguments.arguments(defineThatIt(isADecimalNumberEqualTo(1)).otherwiseReport("I am error"), .2),
      Arguments.arguments(defineThatIt(isADecimalNumberBetween(5).and(.55)).otherwiseReport("I am error"), .2),
      Arguments.arguments(defineThatIt(isADecimalNumberBetween(.55).and(.5)).otherwiseReport("I am error"), .2),
      Arguments.arguments(defineThatIt(isADecimalNumberContaining(54)).otherwiseReport("I am error"), .2),
      Arguments.arguments(defineThatIt(isADecimalNumberNotContaining(2)).otherwiseReport("I am error"), .2),
      Arguments.arguments(defineThatIt(isNotNull()).otherwiseReport("I am error"), null),
      Arguments.arguments(defineThatIt(isNull()).otherwiseReport("I am error"), .2),
      Arguments.arguments(defineThatIt(isADecimalNumberContainingOnly(0)).otherwiseReport("I am error"), .2)
    );
  }

  @ParameterizedTest
  @MethodSource("validTestCases")
  void validate_doubleMatchingRule_shouldReturnTrue(Rule<Object> rule, Object value) {
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule).validate()).isTrue();
  }

  @ParameterizedTest
  @MethodSource("invalidTestCases")
  void validate_doubleNotMatchingRule_shouldReturnFalse(Rule<Object> rule, Object value) {
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule).validate()).isFalse();
  }

  @Test
  void validate_dualInputTests() {
    var value = .123456789;
    var rule1 = defineThatIt(isADecimalNumberContaining(
      1,
      2,
      3,
      4,
      5,
      6,
      7,
      8,
      9,
      12,
      23,
      789
    )).otherwiseReport("I am error");
    var rule2 = defineThatIt(isADecimalNumberContaining(
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
    )).otherwiseReport("I am error");
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule1).validate()).isTrue();
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule2).validate()).isFalse();
  }
}
