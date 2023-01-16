package com.compilit.validation.predicates;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.compilit.validation.api.Definitions.defineThatIt;
import static com.compilit.validation.api.Verifications.verifyThat;
import static testutil.TestValue.TEST_CONTENT;

class StringLengthConstraintPredicateBuilderTests {

  public static Stream<Arguments> validTestCases() {
    return Stream.of(
            Arguments.arguments(0, 5),
            Arguments.arguments(5, 0),
            Arguments.arguments(1, 5),
            Arguments.arguments(5, 1),
            Arguments.arguments(2, 5),
            Arguments.arguments(5, 2),
            Arguments.arguments(3, 5),
            Arguments.arguments(5, 3),
            Arguments.arguments(4, 5),
            Arguments.arguments(5, 4),
            Arguments.arguments(0, 6),
            Arguments.arguments(6, 0),
            Arguments.arguments(0, 7),
            Arguments.arguments(7, 0),
            Arguments.arguments(0, 8),
            Arguments.arguments(8, 0)
    );
  }

  public static Stream<Arguments> invalidTestCases() {
    return Stream.of(
            Arguments.arguments(10, 5),
            Arguments.arguments(5, 10),
            Arguments.arguments(5, 5),
            Arguments.arguments(0, 0),
            Arguments.arguments(1, 1),
            Arguments.arguments(10, 10)
    );
  }

  @ParameterizedTest
  @MethodSource("validTestCases")
  void and_stringMatchingRule_shouldReturnTrue(int first, int second) {
    var rule0 = defineThatIt(StringPredicate.hasALengthBetween(first).and(second)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(TEST_CONTENT).compliesWith(rule0).validate()).isTrue();
  }

  @ParameterizedTest
  @MethodSource("invalidTestCases")
  void and_stringNotMatchingRule_shouldReturnFalse(int first, int second) {
    var rule0 = defineThatIt(StringPredicate.hasALengthBetween(first).and(second)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(TEST_CONTENT).compliesWith(rule0).validate()).isFalse();
  }

}
