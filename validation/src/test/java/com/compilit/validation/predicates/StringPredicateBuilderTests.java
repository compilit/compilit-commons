package com.compilit.validation.predicates;

import com.compilit.core.api.validation.Rule;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.compilit.validation.api.Definitions.defineThatIt;
import static com.compilit.validation.api.Verifications.verifyThat;
import static com.compilit.validation.predicates.StringPredicate.*;
import static org.junit.jupiter.params.provider.Arguments.*;
import static testutil.TestValue.TEST_CONTENT;

class StringPredicateBuilderTests {

  public static Stream<Arguments> validTestCases() {
    return Stream.of(
      arguments(defineThatIt(isEqualTo(TEST_CONTENT)).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(contains(TEST_CONTENT, TEST_CONTENT)).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(doesNotContain("?")).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(hasALengthOf(TEST_CONTENT.length()).and(contains("t"))).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(hasALengthBetween(0).and(10)).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(isAlphabetic()).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(isNotNumeric()).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(isNotNull()).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(isNotNullEmptyOrBlank()).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(isNullEmptyOrBlank()).otherwiseReport("failure"), ""),
      arguments(defineThatIt(isNullEmptyOrBlank()).otherwiseReport("failure"), " "),
      arguments(defineThatIt(isNullEmptyOrBlank()).otherwiseReport("failure"), null),
      arguments(defineThatIt(containsOnly(TEST_CONTENT)).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(containsOnly("1" ,"2", "3")).otherwiseReport("failure"), "123"),
      arguments(defineThatIt(containsOnly("1" ,"2", "3")).otherwiseReport("failure"), "123"),
      arguments(defineThatIt(containsOnly("1" ,"2", "3")).otherwiseReport("failure"), List.of("1", "2", "3"))
    );
  }

  public static Stream<Arguments> invalidTestCases() {
    return Stream.of(
      arguments(defineThatIt(isEqualTo("?")).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(contains("?", "bla")).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(contains("?")).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(doesNotContain(TEST_CONTENT, TEST_CONTENT)).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(hasALengthOf(123).and(contains("t"))).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(hasALengthBetween(100).and(200)).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(isNotAlphabetic()).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(isNumeric()).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(isNotNull()).otherwiseReport("failure"), null),
      arguments(defineThatIt(isNotNullEmptyOrBlank()).otherwiseReport("failure"), ""),
      arguments(defineThatIt(isNullEmptyOrBlank()).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(containsOnly( "something else")).otherwiseReport("failure"), TEST_CONTENT),
      arguments(defineThatIt(containsOnly( "1", "2", "3")).otherwiseReport("failure"), "1234"),
      arguments(defineThatIt(containsOnly( "1", "2", "3")).otherwiseReport("failure"), List.of("1", "2", "3", "4"))
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
    var value = TEST_CONTENT;
    var rule0 = defineThatIt(isEqualTo("test")).otherwiseReport("failure");
    Supplier<String> supplier = () -> value;
    Assertions.assertThat(verifyThat(value).compliesWith(rule0).andThen(supplier).orElseThrow(RuntimeException::new)).isEqualTo(value);
  }

  @Test
  void andThen_stringNotMatchingRule_shouldReturnOtherObject() {
    var value = TEST_CONTENT;
    var otherValue = TEST_CONTENT + TEST_CONTENT;
    var rule0 = defineThatIt(isEqualTo("blah")).otherwiseReport("failure");
    Supplier<String> supplier = () -> value;
    Assertions.assertThat(verifyThat(value).compliesWith(rule0).andThen(supplier).orElseReturn(otherValue)).isEqualTo(otherValue);
    Assertions.assertThat(verifyThat(value).compliesWith(rule0).andThen(supplier).orElseReturn(otherValue)).isNotEqualTo(value);
  }

}
