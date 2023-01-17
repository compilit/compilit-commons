package com.compilit.functions;

import static com.compilit.functions.Sanitizers.sanitize;
import static com.compilit.functions.Sanitizers.softSanitize;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SanitizersTest {

  public static Stream<Arguments> validTestCases() {
    return Stream.of(
      Arguments.arguments("Test\u200B", "\u200B"),
      Arguments.arguments("Test\u200C", "\u200C"),
      Arguments.arguments("Test\u200D", "\u200D"),
      Arguments.arguments("Test\u2060", "\u2060"),
      Arguments.arguments("Test\uFEFF", "\uFEFF"),
      Arguments.arguments("Test\r", "\r"),
      Arguments.arguments("Test\t", "\t"),
      Arguments.arguments("Test\b", "\b")
    );
  }

  public static Stream<Arguments> validSoftSanitizeTestCases() {
    return Stream.of(
      Arguments.arguments("Test\rTest", "\r"),
      Arguments.arguments("Test\tTest", "\t"),
      Arguments.arguments("Test\nTest", "\n")
    );
  }

  @ParameterizedTest
  @MethodSource("validTestCases")
  void sanitize_stringContainingZeroWidthSpace_shouldReturnStringWithoutZeroWidthSpace(String input,
                                                                                       String nonPrintableChar) {
    assertThat(input).contains(nonPrintableChar);
    assertThat(sanitize(input)).doesNotContain(nonPrintableChar);
  }

  @ParameterizedTest
  @MethodSource("validSoftSanitizeTestCases")
  void softSanitize_stringContainingZeroWidthSpace_shouldReturnStringWithoutZeroWidthSpace(String input,
                                                                                           String nonPrintableChar) {
    assertThat(input).contains(nonPrintableChar);
    assertThat(softSanitize(input)).contains(nonPrintableChar);
  }
}
