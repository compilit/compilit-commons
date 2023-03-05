package com.compilit.sanitization;

import static com.compilit.sanitization.Sanitizers.sanitize;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SanitizersTest {

  private static final String TEST_VALUE = "Test";

  public static Stream<Arguments> validSanitizeNonPrintableTestCases() {
    return Stream.of(
      Arguments.arguments(TEST_VALUE + "\u200B", "\u200B"),
      Arguments.arguments(TEST_VALUE + "\u200C", "\u200C"),
      Arguments.arguments(TEST_VALUE + "\u200D", "\u200D"),
      Arguments.arguments(TEST_VALUE + "\u2060", "\u2060"),
      Arguments.arguments(TEST_VALUE + "\uFEFF", "\uFEFF"),
      Arguments.arguments(TEST_VALUE + "\r", "\r"),
      Arguments.arguments(TEST_VALUE + "\t", "\t"),
      Arguments.arguments(TEST_VALUE + "\b", "\b")
    );
  }

  @ParameterizedTest
  @MethodSource("validSanitizeNonPrintableTestCases")
  void sanitize_stringContainingZeroWidthSpace_shouldReturnStringWithoutZeroWidthSpace(String input,
                                                                                       String nonPrintableChar) {
    assertThat(input).contains(nonPrintableChar);
    var result = sanitize(input);
    assertThat(result).doesNotContain(nonPrintableChar);
    assertThat(result).isEqualTo("Test");
  }

}
