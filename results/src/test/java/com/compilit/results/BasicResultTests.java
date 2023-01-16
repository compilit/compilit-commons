package com.compilit.results;

import static com.compilit.results.Messages.MESSAGE_FORMAT_ERROR;

import com.compilit.results.assertions.ResultAssertions;
import com.compilit.results.testutil.TestValue;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class BasicResultTests {

  @Test
  void success_shouldReturnSuccessResult() {
    ResultAssertions.assertThat(Result.success()).isValidSuccessResult()
                    .isEmpty();
  }

  @Test
  void success_shouldReturnSuccessResultWithContents() {
    ResultAssertions.assertThat(Result.success(TestValue.TEST_CONTENT))
                    .isValidSuccessResult()
                    .containsContent(TestValue.TEST_CONTENT);
  }

  @Test
  void notFound_shouldReturnNotFoundResult() {
    ResultAssertions.assertThat(Result.notFound()).isValidUnsuccessfulResult()
                    .isEmpty();
  }

  @Test
  void notFound_withMessage_shouldReturnUnsuccessfulResult() {
    ResultAssertions.assertThat(Result.notFound(TestValue.TEST_MESSAGE))
                    .isValidUnsuccessfulResult()
                    .containsMessage(TestValue.TEST_MESSAGE);
  }

  @Test
  void notFound_withNullMessage_shouldReturnDefaultMessage() {
    var expected = Messages.NO_MESSAGE_AVAILABLE;
    var result = Result.notFound(null);
    Assertions.assertThat(result.getMessage()).isEqualTo(expected);
    Assertions.assertThat(result.getResultStatus()).isEqualTo(ResultStatus.NOT_FOUND);
  }

  @Test
  void notFound_withoutFormatArgs_shouldReturnMessage() {
    var expected = "test";
    var result = Result.notFound(expected);
    Assertions.assertThat(result.getMessage()).isEqualTo(expected);
    Assertions.assertThat(result.getResultStatus()).isEqualTo(ResultStatus.NOT_FOUND);
  }

  @Test
  void notFound_withNullFormatArgs_shouldReturnMessage() {
    var expected = "test";
    var result = Result.notFound(expected, null);
    Assertions.assertThat(result.getMessage()).isEqualTo(expected);
    Assertions.assertThat(result.getResultStatus()).isEqualTo(ResultStatus.NOT_FOUND);
  }

  @Test
  void notFound_withFormattedMessage_shouldReturnFormattedMessage() {
    var expected = String.format("test %s", "test");
    var result = Result.notFound("test %s", "test");
    Assertions.assertThat(result.getMessage()).isEqualTo(expected);
    Assertions.assertThat(result.getResultStatus()).isEqualTo(ResultStatus.NOT_FOUND);
  }

  @Test
  void notFound_withInvalidFormatPlaceholders_shouldReturnMessageContainingExceptionMessage() {
    var message = "test %s, %s";
    var expected = MESSAGE_FORMAT_ERROR;
    var result = Result.notFound(message, "test");
    Assertions.assertThat(result.getMessage()).contains(expected);
    Assertions.assertThat(result.getResultStatus()).isEqualTo(ResultStatus.NOT_FOUND);
  }

  @Test
  void unprocessable_shouldReturnUnprocessableResult() {
    ResultAssertions.assertThat(Result.unprocessable(TestValue.TEST_MESSAGE))
                    .isValidUnsuccessfulResult()
                    .containsMessage(TestValue.TEST_MESSAGE);
  }

  @Test
  void unprocessable_withMessage_shouldReturnUnsuccessfulResult() {
    ResultAssertions.assertThat(Result.unprocessable(TestValue.TEST_MESSAGE))
                    .isValidUnsuccessfulResult()
                    .containsMessage(TestValue.TEST_MESSAGE);
  }

  @Test
  void unprocessable_withFormattedMessage_shouldReturnFormattedMessage() {
    var expected = String.format("test %s", "test");
    var result = Result.unprocessable("test %s", "test");
    Assertions.assertThat(result.getMessage()).isEqualTo(expected);
    Assertions.assertThat(result.getResultStatus()).isEqualTo(ResultStatus.UNPROCESSABLE);
  }

  @Test
  void unauthorized_withFormattedMessage_shouldReturnFormattedMessage() {
    var expected = String.format("test %s", "test");
    var result = Result.unauthorized("test %s", "test");
    Assertions.assertThat(result.getMessage()).isEqualTo(expected);
    Assertions.assertThat(result.getResultStatus()).isEqualTo(ResultStatus.UNAUTHORIZED);
  }

  @Test
  void unauthorized_shouldReturnunauthorizedResult() {
    ResultAssertions.assertThat(Result.unauthorized()).isValidUnsuccessfulResult()
                    .isEmpty();
  }

  @Test
  void unauthorized_withMessage_shouldReturnUnsuccessfulResult() {
    ResultAssertions.assertThat(Result.unauthorized(TestValue.TEST_MESSAGE))
                    .isValidUnsuccessfulResult()
                    .containsMessage(TestValue.TEST_MESSAGE);
  }

  @Test
  void errorOccurred_shouldReturnSuccessResult() {
    ResultAssertions.assertThat(Result.errorOccurred(TestValue.TEST_MESSAGE))
                    .isValidUnsuccessfulResult()
                    .containsMessage(TestValue.TEST_MESSAGE);
  }

  @Test
  void errorOccurred_withFormattedMessage_shouldReturnFormattedMessage() {
    var expected = String.format("test %s", "test");
    var result = Result.errorOccurred("test %s", "test");
    Assertions.assertThat(result.getMessage()).isEqualTo(expected);
    Assertions.assertThat(result.getResultStatus()).isEqualTo(ResultStatus.ERROR_OCCURRED);
  }

}
