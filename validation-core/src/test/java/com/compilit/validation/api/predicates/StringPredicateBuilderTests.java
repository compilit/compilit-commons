package com.compilit.validation.api.predicates;

import static com.compilit.validation.Definitions.defineThatIt;
import static com.compilit.validation.Verifications.verifyThat;
import static com.compilit.validation.api.predicates.StringPredicate.contains;
import static com.compilit.validation.api.predicates.StringPredicate.containsOnly;
import static com.compilit.validation.api.predicates.StringPredicate.doesNotContain;
import static com.compilit.validation.api.predicates.StringPredicate.hasALengthBetween;
import static com.compilit.validation.api.predicates.StringPredicate.hasALengthOf;
import static com.compilit.validation.api.predicates.StringPredicate.isAlphabetic;
import static com.compilit.validation.api.predicates.StringPredicate.isEqualTo;
import static com.compilit.validation.api.predicates.StringPredicate.isNotAlphabetic;
import static com.compilit.validation.api.predicates.StringPredicate.isNotNull;
import static com.compilit.validation.api.predicates.StringPredicate.isNotNullEmptyOrBlank;
import static com.compilit.validation.api.predicates.StringPredicate.isNotNumeric;
import static com.compilit.validation.api.predicates.StringPredicate.isNullEmptyOrBlank;
import static com.compilit.validation.api.predicates.StringPredicate.isNumeric;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.compilit.validation.api.Rule;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import testutil.TestValue;

class StringPredicateBuilderTests {

  public static Stream<Arguments> validTestCases() {
    return Stream.of(
      arguments(defineThatIt(isEqualTo(TestValue.TEST_CONTENT)).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(
        defineThatIt(contains(TestValue.TEST_CONTENT, TestValue.TEST_CONTENT)).otherwiseReport("failure"),
        TestValue.TEST_CONTENT
      ),
      arguments(defineThatIt(doesNotContain("?")).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(
        defineThatIt(hasALengthOf(TestValue.TEST_CONTENT.length()).and(contains("t"))).otherwiseReport("failure"),
        TestValue.TEST_CONTENT
      ),
      arguments(defineThatIt(hasALengthBetween(0).and(10)).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(defineThatIt(isAlphabetic()).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(defineThatIt(isNotNumeric()).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(defineThatIt(isNotNull()).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(defineThatIt(isNotNullEmptyOrBlank()).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(defineThatIt(isNullEmptyOrBlank()).otherwiseReport("failure"), ""),
      arguments(defineThatIt(isNullEmptyOrBlank()).otherwiseReport("failure"), " "),
      arguments(defineThatIt(isNullEmptyOrBlank()).otherwiseReport("failure"), null),
      arguments(defineThatIt(containsOnly(TestValue.TEST_CONTENT)).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(defineThatIt(containsOnly("1", "2", "3")).otherwiseReport("failure"), "123"),
      arguments(defineThatIt(containsOnly("1", "2", "3")).otherwiseReport("failure"), "123"),
      arguments(defineThatIt(containsOnly("1", "2", "3")).otherwiseReport("failure"), List.of("1", "2", "3"))
    );
  }

  public static Stream<Arguments> invalidTestCases() {
    return Stream.of(
      arguments(defineThatIt(isEqualTo("?")).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(defineThatIt(contains("?", "bla")).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(defineThatIt(contains("?")).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(
        defineThatIt(doesNotContain(TestValue.TEST_CONTENT, TestValue.TEST_CONTENT)).otherwiseReport("failure"),
        TestValue.TEST_CONTENT
      ),
      arguments(defineThatIt(hasALengthOf(123).and(contains("t"))).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(defineThatIt(hasALengthBetween(100).and(200)).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(defineThatIt(isNotAlphabetic()).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(defineThatIt(isNumeric()).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(defineThatIt(isNotNull()).otherwiseReport("failure"), null),
      arguments(defineThatIt(isNotNullEmptyOrBlank()).otherwiseReport("failure"), ""),
      arguments(defineThatIt(isNullEmptyOrBlank()).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(defineThatIt(containsOnly("something else")).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(defineThatIt(containsOnly("1", "2", "3")).otherwiseReport("failure"), "1234"),
      arguments(defineThatIt(containsOnly("1", "2", "3")).otherwiseReport("failure"), List.of("1", "2", "3", "4"))
    );
  }

  @ParameterizedTest
  @MethodSource("validTestCases")
  void validate_stringMatchingRule_shouldReturnTrue(Rule<Object> rule, Object value) {
    Assertions.assertThat(verifyThat(value).compliesWith(rule).validate()).isTrue();
  }

  @ParameterizedTest
  @MethodSource("invalidTestCases")
  void validate_stringNotMatchingRule_shouldReturnFalse(Rule<Object> rule, Object value) {
    Assertions.assertThat(verifyThat(value).compliesWith(rule).validate()).isFalse();
  }

  @Test
  void andThen_stringMatchingRule_shouldReturnWantedObject() {
    var value = TestValue.TEST_CONTENT;
    var rule0 = defineThatIt(isEqualTo("test")).otherwiseReport("failure");
    Supplier<String> supplier = () -> value;
    Assertions.assertThat(verifyThat(value).compliesWith(rule0).andThen(supplier).orElseThrow(RuntimeException::new))
              .isEqualTo(value);
  }

  @Test
  void andThen_stringNotMatchingRule_shouldReturnOtherObject() {
    var value = TestValue.TEST_CONTENT;
    var otherValue = TestValue.TEST_CONTENT + TestValue.TEST_CONTENT;
    var rule0 = defineThatIt(isEqualTo("blah")).otherwiseReport("failure");
    Supplier<String> supplier = () -> value;
    Assertions.assertThat(verifyThat(value).compliesWith(rule0).andThen(supplier).orElseReturn(otherValue))
              .isEqualTo(otherValue);
    Assertions.assertThat(verifyThat(value).compliesWith(rule0).andThen(supplier).orElseReturn(otherValue))
              .isNotEqualTo(value);
  }

}
