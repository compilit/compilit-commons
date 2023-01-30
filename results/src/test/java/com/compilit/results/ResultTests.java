package com.compilit.results;

import static com.compilit.results.testutil.TestValue.TEST_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

import com.compilit.results.assertions.ResultAssertions;
import com.compilit.results.testutil.TestValue;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ResultTests {

  @Test
  void fromResult_shouldReturnEmptyResultWithCorrectStatus() {
    var result = Result.success("test");
    assertThat(result.isEmpty()).isFalse();
    var actual = Result.<Integer>transform(result);
    assertThat(actual.isEmpty()).isTrue();
    assertThat(actual.isSuccessful()).isTrue();
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
    var actual = Result.merge(resultOne, resultTwo, resultThree);

    ResultAssertions.assertThat(actual).isValidSuccessResult()
                    .hasContent();
    assertThat(actual.getContents(x -> x.orElse(new ArrayList<>())).size()).isEqualTo(3);
  }

  @Test
  void merge_errorOccurred_shouldCombineMultipleResults() {
    var contentsOne = TestValue.TEST_CONTENT + 1;
    var contentsTwo = TestValue.TEST_CONTENT + 2;
    var contentsThree = TestValue.TEST_CONTENT + 3;
    var resultOne = Result.<String>errorOccurred(contentsOne);
    var resultTwo = Result.success(contentsTwo);
    var resultThree = Result.success(contentsThree);
    var actual = Result.merge(resultOne, resultTwo, resultThree);

    ResultAssertions.assertThat(actual).isValidUnsuccessfulResult()
                    .isEmpty();
  }

  @Test
  void mergeWith_success_shouldCombineMultipleResults() {
    var contentsOne = TestValue.TEST_CONTENT + 1;
    var contentsTwo = TestValue.TEST_CONTENT + 2;
    var contentsThree = TestValue.TEST_CONTENT + 3;
    var resultOne = Result.<String>success(contentsOne);
    var resultTwo = Result.success(contentsTwo);
    var resultThree = Result.success(contentsThree);
    var actual = resultOne.mergeWith(resultTwo, resultThree);

    assertThat(actual.isSuccessful()).isTrue();
  }

  @Test
  void mergeWith_errorOccurred_shouldCombineMultipleResults() {
    var contentsOne = TestValue.TEST_CONTENT + 1;
    var contentsTwo = TestValue.TEST_CONTENT + 2;
    var contentsThree = TestValue.TEST_CONTENT + 3;
    var resultOne = Result.<String>errorOccurred(contentsOne);
    var resultTwo = Result.success(contentsTwo);
    var resultThree = Result.success(contentsThree);
    var actual = resultOne.mergeWith(resultTwo, resultThree);

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
    var actual = Result.sum(resultOne, resultTwo, resultThree);

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
    var actual = Result.sum(resultOne, resultTwo, resultThree);

    ResultAssertions.assertThat(actual).isValidUnsuccessfulResult()
                    .isEmpty();
  }

}
