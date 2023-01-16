package com.compilit.results;

import static com.compilit.results.assertions.ResultAssertions.assertThat;
import static com.compilit.results.testutil.TestValue.TEST_MESSAGE;

import org.junit.jupiter.api.Test;

public class ResultOfTest {

  @Test
  void resultOf_notFoundException_shouldReturnNotFoundResult() {
    var actual = Result.resultOf(() -> {throw new NotFoundException(TEST_MESSAGE);});
    assertThat(actual).isANotFoundResult();
  }

  @Test
  void resultOf_unprocessableException_shouldReturnUnprocessableResult() {
    var actual = Result.resultOf(() -> {throw new UnprocessableException(TEST_MESSAGE);});
    assertThat(actual).isAnUnprocessableResult();
  }

  @Test
  void resultOf_unauthorizedException_shouldReturnUnauthorizedResult() {
    var actual = Result.resultOf(() -> {throw new UnauthorizedException(TEST_MESSAGE);});
    assertThat(actual).isAnUnauthorizedResult();
  }

  @Test
  void resultOf_errorOccurredException_shouldReturnErrorOccurredResult() {
    var actual = Result.resultOf(() -> {throw new ErrorOccurredException(TEST_MESSAGE);});
    assertThat(actual).isAnErrorOccurredResult();
  }
}
