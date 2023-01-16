package com.compilit.results;

import static com.compilit.results.testutil.TestValue.TEST_MESSAGE;

import com.compilit.results.assertions.ResultAssertions;
import com.compilit.results.testutil.TestValue;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;
import java.util.function.Supplier;

class ResultTests {
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

  @Test
  void fromResult_shouldReturnEmptyResultWithCorrectStatus() {
    var result = Result.success("test");
    Assertions.assertThat(result.isEmpty()).isFalse();
    var actual = Result.<Integer>transform(result);
    Assertions.assertThat(actual.isEmpty()).isTrue();
    Assertions.assertThat(actual.isSuccessful()).isTrue();
  }

  @Test
  void fromResult_withoutContent_shouldReturnSameStatusWithoutContent() {
    var result = Result.success(TestValue.TEST_CONTENT);
    var actual = Result.<Integer>transform(result);
    ResultAssertions.assertThat(actual).isEmpty()
            .isValidSuccessResult();
  }

  @Test
  void merge_shouldCombineMultipleResults() {
    var contentsOne = TestValue.TEST_CONTENT + 1;
    var contentsTwo = TestValue.TEST_CONTENT + 2;
    var contentsThree = TestValue.TEST_CONTENT + 3;
    var resultOne = Result.success(contentsOne);
    var resultTwo = Result.success(contentsTwo);
    var resultThree = Result.success(contentsThree);
    var actual = Result.combine(resultOne).with(resultTwo).and(resultThree).merge();

    ResultAssertions.assertThat(actual).isValidSuccessResult()
            .hasContent();
    Assertions.assertThat(actual.getContents(x -> x.orElse(new ArrayList<>())).size()).isEqualTo(3);
  }

  @Test
  void merge_errorOccurred_shouldCombineMultipleResults() {
    var contentsOne = TestValue.TEST_CONTENT + 1;
    var contentsTwo = TestValue.TEST_CONTENT + 2;
    var contentsThree = TestValue.TEST_CONTENT + 3;
    var resultOne = Result.<String>errorOccurred(contentsOne);
    var resultTwo = Result.success(contentsTwo);
    var resultThree = Result.success(contentsThree);
    var actual = Result.combine(resultOne).with(resultTwo).and(resultThree).merge();

    ResultAssertions.assertThat(actual).isValidUnsuccessfulResult()
            .isEmpty();
  }

  @Test
  void sum_shouldCombineMultipleResults() {
    var contentsOne = TestValue.TEST_CONTENT + 1;
    var contentsTwo = TestValue.TEST_CONTENT + 2;
    var contentsThree = TestValue.TEST_CONTENT + 3;
    var resultOne = Result.success(contentsOne);
    var resultTwo = Result.success(contentsTwo);
    var resultThree = Result.success(contentsThree);
    var actual = Result.combine(resultOne).with(resultTwo).and(resultThree).sum();

    ResultAssertions.assertThat(actual).isValidSuccessResult()
            .isEmpty();
  }

  @Test
  void sum_successResults_shouldCombineMultipleResults() {
    var resultOne = Result.success();
    var resultTwo = Result.success();
    var resultThree = Result.success();
    var actual = Result.sum(resultOne, resultTwo, resultThree);
    ResultAssertions.assertThat(actual).isValidSuccessResult()
                    .isEmpty();
  }

  @Test
  void sum_oneErrorOccurredResult_shouldCombineMultipleResults() {
    var resultOne = Result.success();
    var resultTwo = Result.errorOccurred(TEST_MESSAGE);
    var resultThree = Result.success();
    var actual = Result.sum(resultOne, resultTwo, resultThree);
    ResultAssertions.assertThat(actual).isValidUnsuccessfulResult()
                    .isEmpty();
  }

  @Test
  void sum_errorOccurred_shouldCombineMultipleResults() {
    var contentsOne = TestValue.TEST_CONTENT + 1;
    var contentsTwo = TestValue.TEST_CONTENT + 2;
    var contentsThree = TestValue.TEST_CONTENT + 3;
    var resultOne = Result.<String>errorOccurred(contentsOne);
    var resultTwo = Result.success(contentsTwo);
    var resultThree = Result.success(contentsThree);
    var actual = Result.combine(resultOne).with(resultTwo).and(resultThree).sum();

    ResultAssertions.assertThat(actual).isValidUnsuccessfulResult()
            .isEmpty();
  }

}
