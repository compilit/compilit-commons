package com.compilit.results;

import static com.compilit.results.assertions.ResultAssertions.assertThat;
import static com.compilit.results.testutil.TestValue.TEST_MESSAGE;

import com.compilit.results.assertions.ResultAssertions;
import com.compilit.results.testutil.TestValue;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class ResultOfTest {

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

  @Test
  void resultOf_orElseNotFound_shouldReturnNotFoundResult() {
    var actual = Result.resultOf(() -> Result.errorOccurred(TEST_MESSAGE).orElseNotFound());
    assertThat(actual).isANotFoundResult();
  }

  @Test
  void resultOf_orElseUnauthorized_shouldReturnUnauthorizedResult() {
    var actual = Result.resultOf(() -> Result.errorOccurred(TEST_MESSAGE).orElseUnauthorized());
    assertThat(actual).isAnUnauthorizedResult();
  }

  @Test
  void resultOf_orElseUnprocessable_shouldReturnUnprocessableResult() {
    var actual = Result.resultOf(() -> Result.errorOccurred(TEST_MESSAGE).orElseUnprocessable());
    assertThat(actual).isAnUnprocessableResult();
  }

  @Test
  void resultOf_orElseErrorOccurred_shouldReturnErrorOccurredResult() {
    var actual = Result.resultOf(() -> Result.errorOccurred(TEST_MESSAGE).orElseErrorOccurred());
    assertThat(actual).isAnErrorOccurredResult();
  }

  @Test
  void resultOf_SuccessfulRunnable_shouldReturnSuccessResult() {
    var result = Result.resultOf(() -> System.out.println(TestValue.TEST_CONTENT));
    ResultAssertions.assertThat(result).isValidSuccessResult()
                    .isEmpty();
  }

  @Test
  void resultOf_ExceptionalRunnable_shouldReturnSuccessResult() {
    var result = Result.resultOf(() -> System.out.println(TestValue.TEST_CONTENT));
    ResultAssertions.assertThat(result).isValidSuccessResult()
                    .isEmpty();
  }

  @Test
  void resultOf_SuccessfulPredicate_shouldReturnSuccessResult() {
    var result = Result.resultOf(x -> true, null);
    ResultAssertions.assertThat(result).isValidSuccessResult()
                    .isEmpty();
  }

  @Test
  void resultOf_UnsuccessfulPredicate_shouldReturnUnprocessableResult() {
    var result = Result.resultOf(x -> false, null);
    ResultAssertions.assertThat(result).isValidUnsuccessfulResult()
                    .isEmpty();
  }

  @Test
  void resultOf_ExceptionalPredicate_shouldReturnErrorOccurredResult() {
    var exception = new RuntimeException(TestValue.TEST_CONTENT);
    var throwingPredicate = new Predicate<String>() {
      @Override
      public boolean test(String s) {
        throw exception;
      }
    };
    var message = exception.getMessage();
    var result = Result.resultOf(throwingPredicate, null);
    ResultAssertions.assertThat(result).isValidUnsuccessfulResult().containsMessage(message);
  }

  @Test
  void resultOf_SuccessfulSupplier_shouldReturnSuccessResult() {
    Supplier<String> supplier = () -> TestValue.TEST_CONTENT;
    var result = Result.resultOf(supplier);
    ResultAssertions.assertThat(result)
                    .isValidSuccessResult()
                    .containsContent(TestValue.TEST_CONTENT);
  }

  @Test
  void resultOf_UnsuccessfulSupplier_shouldReturnErrorOccurredResult() {
    var exception = new RuntimeException(TestValue.TEST_CONTENT);
    var Supplier = new Supplier<String>() {
      @Override
      public String get() {
        throw exception;
      }
    };
    var message = exception.getMessage();
    var result = Result.resultOf(Supplier);
    ResultAssertions.assertThat(result).isValidUnsuccessfulResult().containsMessage(message);
  }

  @Test
  void resultOf_UnsuccessfulRunnable_shouldReturnErrorOccurredResult() {
    var exception = new RuntimeException(TestValue.TEST_CONTENT);
    var Runnable = new Runnable() {
      @Override
      public void run() {
        throw exception;
      }
    };
    var message = exception.getMessage();
    var result = Result.resultOf(Runnable);
    ResultAssertions.assertThat(result).isValidUnsuccessfulResult().containsMessage(message);
  }

}
