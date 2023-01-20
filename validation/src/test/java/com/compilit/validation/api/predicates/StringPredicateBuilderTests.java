package com.compilit.validation.api.predicates;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.compilit.validation.Definitions;
import com.compilit.validation.Verifications;
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
      arguments(Definitions.defineThatIt(StringPredicate.isEqualTo(TestValue.TEST_CONTENT)).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(Definitions.defineThatIt(StringPredicate.contains(TestValue.TEST_CONTENT, TestValue.TEST_CONTENT)).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(Definitions.defineThatIt(StringPredicate.doesNotContain("?")).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(
        Definitions.defineThatIt(StringPredicate.hasALengthOf(TestValue.TEST_CONTENT.length()).and(StringPredicate.contains("t"))).otherwiseReport("failure"),
        TestValue.TEST_CONTENT
      ),
      arguments(Definitions.defineThatIt(StringPredicate.hasALengthBetween(0).and(10)).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(Definitions.defineThatIt(StringPredicate.isAlphabetic()).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(Definitions.defineThatIt(StringPredicate.isNotNumeric()).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(Definitions.defineThatIt(StringPredicate.isNotNull()).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(Definitions.defineThatIt(StringPredicate.isNotNullEmptyOrBlank()).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(Definitions.defineThatIt(StringPredicate.isNullEmptyOrBlank()).otherwiseReport("failure"), ""),
      arguments(Definitions.defineThatIt(StringPredicate.isNullEmptyOrBlank()).otherwiseReport("failure"), " "),
      arguments(Definitions.defineThatIt(StringPredicate.isNullEmptyOrBlank()).otherwiseReport("failure"), null),
      arguments(Definitions.defineThatIt(StringPredicate.containsOnly(TestValue.TEST_CONTENT)).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(Definitions.defineThatIt(StringPredicate.containsOnly("1", "2", "3")).otherwiseReport("failure"), "123"),
      arguments(Definitions.defineThatIt(StringPredicate.containsOnly("1", "2", "3")).otherwiseReport("failure"), "123"),
      arguments(Definitions.defineThatIt(StringPredicate.containsOnly("1", "2", "3")).otherwiseReport("failure"), List.of("1", "2", "3"))
    );
  }

  public static Stream<Arguments> invalidTestCases() {
    return Stream.of(
      arguments(Definitions.defineThatIt(StringPredicate.isEqualTo("?")).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(Definitions.defineThatIt(StringPredicate.contains("?", "bla")).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(Definitions.defineThatIt(StringPredicate.contains("?")).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(Definitions.defineThatIt(StringPredicate.doesNotContain(TestValue.TEST_CONTENT, TestValue.TEST_CONTENT)).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(Definitions.defineThatIt(StringPredicate.hasALengthOf(123).and(StringPredicate.contains("t"))).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(Definitions.defineThatIt(StringPredicate.hasALengthBetween(100).and(200)).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(Definitions.defineThatIt(StringPredicate.isNotAlphabetic()).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(Definitions.defineThatIt(StringPredicate.isNumeric()).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(Definitions.defineThatIt(StringPredicate.isNotNull()).otherwiseReport("failure"), null),
      arguments(Definitions.defineThatIt(StringPredicate.isNotNullEmptyOrBlank()).otherwiseReport("failure"), ""),
      arguments(Definitions.defineThatIt(StringPredicate.isNullEmptyOrBlank()).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(Definitions.defineThatIt(StringPredicate.containsOnly("something else")).otherwiseReport("failure"), TestValue.TEST_CONTENT),
      arguments(Definitions.defineThatIt(StringPredicate.containsOnly("1", "2", "3")).otherwiseReport("failure"), "1234"),
      arguments(Definitions.defineThatIt(StringPredicate.containsOnly("1", "2", "3")).otherwiseReport("failure"), List.of("1", "2", "3", "4"))
    );
  }

  @ParameterizedTest
  @MethodSource("validTestCases")
  void validate_stringMatchingRule_shouldReturnTrue(Rule<Object> rule, Object value) {
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule).validate()).isTrue();
  }

  @ParameterizedTest
  @MethodSource("invalidTestCases")
  void validate_stringNotMatchingRule_shouldReturnFalse(Rule<Object> rule, Object value) {
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule).validate()).isFalse();
  }

  @Test
  void andThen_stringMatchingRule_shouldReturnWantedObject() {
    var value = TestValue.TEST_CONTENT;
    var rule0 = Definitions.defineThatIt(StringPredicate.isEqualTo("test")).otherwiseReport("failure");
    Supplier<String> supplier = () -> value;
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule0).andThen(supplier).orElseThrow(RuntimeException::new))
              .isEqualTo(value);
  }

  @Test
  void andThen_stringNotMatchingRule_shouldReturnOtherObject() {
    var value = TestValue.TEST_CONTENT;
    var otherValue = TestValue.TEST_CONTENT + TestValue.TEST_CONTENT;
    var rule0 = Definitions.defineThatIt(StringPredicate.isEqualTo("blah")).otherwiseReport("failure");
    Supplier<String> supplier = () -> value;
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule0).andThen(supplier).orElseReturn(otherValue))
              .isEqualTo(otherValue);
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule0).andThen(supplier).orElseReturn(otherValue))
              .isNotEqualTo(value);
  }

}
