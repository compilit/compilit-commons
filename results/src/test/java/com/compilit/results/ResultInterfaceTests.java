package com.compilit.results;

import static com.compilit.results.Messages.NOTHING_TO_REPORT;
import static com.compilit.results.testutil.TestValue.TEST_CONTENT;
import static com.compilit.results.testutil.TestValue.TEST_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.compilit.results.testutil.TestValue;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

public class ResultInterfaceTests {

  @Test
  void isEmpty_emptyResult_shouldReturnTrue() {
    var result = Result.success();
    assertThat(result.isEmpty()).isTrue();
  }

  @Test
  void isEmpty_filledResult_shouldReturnFalse() {
    var result = Result.success("test");
    assertThat(result.isEmpty()).isFalse();
  }

  @Test
  void isSuccessfulWithContents_successfulEmptyResult_shouldReturnFalse() {
    var result = Result.success();
    assertThat(result.isSuccessfulWithContents()).isFalse();
  }

  @Test
  void isSuccessfulWithContents_successfulFilledResult_shouldReturnTrue() {
    var result = Result.success("test");
    assertThat(result.isSuccessfulWithContents()).isTrue();
  }

  @Test
  void getResultStatus_shouldReturnResultStatus() {
    assertThat(Result.success().getResultStatus()).isEqualTo(ResultStatus.SUCCESS);
    assertThat(Result.errorOccurred(TEST_MESSAGE).getResultStatus())
      .isEqualTo(ResultStatus.ERROR_OCCURRED);
    assertThat(Result.success().getResultStatus())
      .isEqualTo(ResultStatus.SUCCESS);
    assertThat(Result.notFound().getResultStatus()).isEqualTo(ResultStatus.NOT_FOUND);
    assertThat(Result.unauthorized().getResultStatus())
      .isEqualTo(ResultStatus.UNAUTHORIZED);
    assertThat(Result.unprocessable().getResultStatus())
      .isEqualTo(ResultStatus.UNPROCESSABLE);
  }

  @Test
  void hasContents_withContents_shouldReturnTrue() {
    var result = Result.success(TestValue.TEST_CONTENT);
    assertThat(result.hasContents()).isTrue();
  }

  @Test
  void hasContents_withoutContents_shouldReturnFalse() {
    var result = Result.success();
    assertThat(result.hasContents()).isFalse();
  }

  @Test
  void isEmpty_withContents_shouldReturnFalse() {
    var result = Result.success(TestValue.TEST_CONTENT);
    assertThat(result.isEmpty()).isFalse();
  }

  @Test
  void isEmpty_withoutContents_shouldReturnTrue() {
    var result = Result.success();
    assertThat(result.isEmpty()).isTrue();
  }

  @Test
  void getOptionalContents_shouldReturnContentsAsOptional() {
    var result = Result.success(TestValue.TEST_CONTENT);
    assertThat(result.getContents()).contains(TestValue.TEST_CONTENT);
  }

  @Test
  void getContents_shouldReturnContents() {
    var result = Result.success(TestValue.TEST_CONTENT);
    assertThat(result.getContents().get()).isEqualTo(TestValue.TEST_CONTENT);
  }

  @Test
  void getContents_intResultToString_shouldReturnString() {
    var input = 123;
    var expected = "123";
    var result = Result.success(input);
    var actual = result.getContents(x -> x.map(String::valueOf).orElse("1"));
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void onSuccessMap_intResultToString_shouldReturnString() {
    var input = 123;
    var expected = "123";
    var result = Result.success(input);
    var actual = result.onSuccessMap(String::valueOf);
    assertThat(actual.getContents().get()).isEqualTo(expected);
  }

  @Test
  void transform_intResultToString_shouldReturnString() {
    var input = 123;
    var result = Result.success(input);
    var actual = result.transform(r -> r.getContents().orElse(-1));
    assertThat(actual).isEqualTo(input);
  }

  @Test
  void onSuccessMap_success_shouldApplyFunction() {
    var expected = "10";
    var result = Result.success(10);
    var actual = result.onSuccessMap(String::valueOf);
    assertThat(actual.getContents().get()).isEqualTo(expected);
  }

  @Test
  void onSuccessMap_errorOccurred_shouldReturnResultWithMessage() {
    var result = Result.errorOccurred(TEST_MESSAGE);
    var actual = result.onSuccessMap(x -> TEST_CONTENT);
    assertThat(actual.getMessage()).isEqualTo(TEST_MESSAGE);
  }

  @Test
  void onSuccessMapOrElse_success_shouldApplyFunction() {
    var expected = "10";
    var result = Result.success(10);
    var actual = result.onSuccessMapOrElse(String::valueOf, () -> "defaultResult");
    assertThat(actual.getContents().get()).isEqualTo(expected);
  }

  @Test
  void onSuccessMapOrElse_errorOccurred_shouldReturnSupplierResult() {
    var result = Result.errorOccurred(TEST_MESSAGE);
    var actual = result.onSuccessMapOrElse(x -> "notExpected", () -> TEST_CONTENT);
    assertThat(actual.getMessage()).isEqualTo(NOTHING_TO_REPORT);
    assertThat(actual.getNullableContents()).isEqualTo(TEST_CONTENT);
  }


  @Test
  void onSuccessRun_success_shouldRunRunnable() {
    AtomicBoolean interactedWith = new AtomicBoolean(false);
    var result = Result.success();
    var actual = result.onSuccessRun(() -> interactedWith.set(true));
    assertTrue(interactedWith.get());
    assertThat(result).isEqualTo(actual);
  }

  @Test
  void onSuccessRun_errorOccurred_shouldNotRunRunnable() {
    AtomicBoolean interactedWith = new AtomicBoolean(false);
    var result = Result.errorOccurred("Whoops");
    result.onSuccessRun(() -> interactedWith.set(true));
    assertFalse(interactedWith.get());
  }

  @Test
  void getNullableContents_null_shouldReturnNull() {
    var errorResult = Result.errorOccurred(TEST_MESSAGE);
    assertThat(errorResult.getNullableContents()).isNull();
  }

  @Test
  void orElseThrow_successResult_shouldNotThrowException() {
    var successResult = Result.success(TEST_CONTENT);
    assertThatNoException().isThrownBy(successResult::orElseThrow);
  }


  @Test
  void orElseThrow$Exception_success_shouldNotThrowException() {
    var successResult = Result.success(TEST_CONTENT);
    assertThatNoException().isThrownBy(() -> successResult.orElseThrow(RuntimeException::new));
  }

  @Test
  void orElseThrow$Exception_errorOccurred_shouldThrowException() {
    var errorResult = Result.errorOccurred(TEST_MESSAGE);
    assertThatThrownBy(() -> errorResult.orElseThrow(RuntimeException::new))
      .isInstanceOf(RuntimeException.class);
  }

  @Test
  void getContentsOrElseThrow_nullContents_shouldThrowException() {
    var errorResult = Result.errorOccurred(TEST_MESSAGE);
    assertThatThrownBy(errorResult::getContentsOrElseThrow).isInstanceOf(
      ErrorOccurredException.class);
  }

  @Test
  void getContentsOrElseThrow_nonNullContents_shouldThrowException() {
    var successResult = Result.success(TEST_CONTENT);
    assertThat(successResult.getContentsOrElseThrow()).isEqualTo(TEST_CONTENT);
  }

  @Test
  void getContentsOrElseThrow$Exception_nullContents_shouldThrowException() {
    var errorResult = Result.errorOccurred(TEST_MESSAGE);
    assertThatThrownBy(() -> errorResult.getContentsOrElseThrow(RuntimeException::new))
      .isInstanceOf(RuntimeException.class);
  }

  @Test
  void getContentsOrElseThrow$Exception_nonNullContents_shouldThrowException() {
    var successResult = Result.success(TEST_CONTENT);
    assertThat(successResult.getContentsOrElseThrow(RuntimeException::new)).isEqualTo(TEST_CONTENT);
  }

  @Test
  void getMessage_shouldReturnMessage() {
    var errorMessage = "I am error";
    var errorResult = Result.errorOccurred(errorMessage);
    assertThat(errorResult.getMessage()).isEqualTo(errorMessage);
    var successResult = Result.success();
    assertThat(successResult.getMessage()).isEqualTo(NOTHING_TO_REPORT);
  }

  @Test
  void getMessage_combined_shouldReturnMessagesCombined() {
    var errorMessage1 = "I am error1";
    var errorMessage2 = "I am error2";
    var errorResult1 = Result.errorOccurred(errorMessage1);
    var errorResult2 = Result.errorOccurred(errorMessage2);
    var actual = Result.merge(errorResult1, errorResult2);
    assertThat(actual.getMessage()).isNotEqualTo(errorMessage1);
    assertThat(actual.getMessage()).isNotEqualTo(errorMessage2);
    assertThat(actual.getMessage()).contains(errorMessage1);
    assertThat(actual.getMessage()).contains(errorMessage2);
    System.out.println(actual.getMessage());
  }

  @Test
  void getMessage_withFormattedMessage_shouldReturnFormattedMessage() {
    var expected = String.format("I am error %s %s %s %s", "test1", "test2", "someValue", "test3");
    var actual = Result.errorOccurred("I am error %s %s %s %s", "test1", "test2", "someValue", "test3");
    assertThat(actual.getMessage()).isEqualTo(expected);
    System.out.println(actual.getMessage());
  }

  @Test
  void onSuccess_success_shouldApplyConsumer() {
    var initial = Result.success(1);
    AtomicBoolean atomicBoolean = new AtomicBoolean();
    initial.onSuccess(x -> atomicBoolean.set(true));
    assertThat(atomicBoolean.get()).isTrue();
  }

  @Test
  void onSuccess_errorOccurred_shouldApplyConsumer() {
    var initial = Result.errorOccurred(TEST_MESSAGE);
    AtomicBoolean atomicBoolean = new AtomicBoolean();
    initial.onSuccess(x -> atomicBoolean.set(true));
    assertThat(atomicBoolean.get()).isFalse();
  }

  @Test
  void onSuccessOrElse_success_shouldApplyConsumer() {
    var initial = Result.success(1);
    AtomicBoolean atomicBoolean = new AtomicBoolean();
    AtomicBoolean atomicBoolean2 = new AtomicBoolean();
    initial.onSuccessOrElse(x -> atomicBoolean.set(true), () -> atomicBoolean2.set(true));
    assertThat(atomicBoolean.get()).isTrue();
    assertThat(atomicBoolean2.get()).isFalse();
  }

  @Test
  void onSuccessOrElse_errorOccurred_shouldApplyConsumer() {
    var initial = Result.errorOccurred(TEST_MESSAGE);
    AtomicBoolean atomicBoolean = new AtomicBoolean();
    AtomicBoolean atomicBoolean2 = new AtomicBoolean();
    initial.onSuccessOrElse(x -> atomicBoolean.set(true), () -> atomicBoolean2.set(true));
    assertThat(atomicBoolean.get()).isFalse();
    assertThat(atomicBoolean2.get()).isTrue();
  }

}
