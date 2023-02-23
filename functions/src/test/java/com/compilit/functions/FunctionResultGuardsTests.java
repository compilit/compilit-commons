package com.compilit.functions;

import static com.compilit.functions.FunctionResultGuards.orDefault;
import static com.compilit.functions.FunctionResultGuards.orHandleException;
import static com.compilit.functions.FunctionResultGuards.orNull;
import static com.compilit.functions.FunctionResultGuards.orNull;
import static org.assertj.core.api.Assertions.assertThat;
import static com.compilit.functions.TestFunctions.DEFAULT_TEST_VALUE;
import static com.compilit.functions.TestFunctions.TEST_VALUE;
import static com.compilit.functions.TestFunctions.checkedExceptionThrowingFunction;
import static com.compilit.functions.TestFunctions.checkedExceptionThrowingRunnable;
import static com.compilit.functions.TestFunctions.checkedExceptionThrowingSupplier;
import static com.compilit.functions.TestFunctions.exteptionThrowingRunnable;
import static com.compilit.functions.TestFunctions.runtimeExceptionThrowingFunction;
import static com.compilit.functions.TestFunctions.runtimeExceptionThrowingSupplier;
import static com.compilit.functions.TestFunctions.testFunction;
import static com.compilit.functions.TestFunctions.testRunnable;
import static com.compilit.functions.TestFunctions.testSupplier;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

public class FunctionResultGuardsTests {

  @Test
  void orNull_noException_shouldReturnValue() {
    assertThat(orNull(testSupplier())).isEqualTo(TEST_VALUE);
  }

  @Test
  void orNull_exception_shouldReturnNull() {
    var actual = orNull(runtimeExceptionThrowingSupplier());
    assertThat(actual).isNull();
  }

  @Test
  void orNull_checkedException_shouldReturnNull() {
    assertThat(FunctionResultGuards.orNullThrowing(checkedExceptionThrowingSupplier())).isNull();
  }

  @Test
  void orDefault_noException_shouldReturnValue() {
    assertThat(orDefault(testSupplier(), DEFAULT_TEST_VALUE)).isEqualTo(TEST_VALUE);
  }

  @Test
  void orDefault_exception_shouldReturnDefaultValue() {
    assertThat(orDefault(runtimeExceptionThrowingSupplier(), DEFAULT_TEST_VALUE)).isEqualTo(DEFAULT_TEST_VALUE);
  }

  @Test
  void orNull_nonThrowingFunction_shouldReturnResult() {
    var result = orNull(testFunction(), 1);
    assertThat(result).isEqualTo("1");
  }

  @Test
  void orNull_throwingFunction_shouldReturnNull() {
    var result = orNull(runtimeExceptionThrowingFunction(), 1);
    assertThat(result).isNull();
  }

  @Test
  void orNull_throwingSupplier_shouldReturnNull() {
    var result = FunctionResultGuards.orNullThrowing(checkedExceptionThrowingSupplier());
    assertThat(result).isNull();
  }

  @Test
  void orDefault_nonThrowingFunction_shouldReturnResult() {
    assertThat(orDefault(testFunction(), 1, "-1")).isEqualTo("1");

  }

  @Test
  void orDefault_throwingFunction_shouldReturnDefault() {
    assertThat(FunctionResultGuards.orDefaultThrowing(checkedExceptionThrowingFunction(), 1, "-1")).isEqualTo("-1");
  }

  @Test
  void orDefault_throwingFunction_shouldReturnNull() {
    var result = FunctionResultGuards.orDefaultThrowing(checkedExceptionThrowingSupplier(), null);
    assertThat(result).isNull();
  }

  @Test
  void orHandleException_nonThrowingFunction_shouldNotHandleException() {
    AtomicReference<Boolean> exceptionHandled = new AtomicReference<>();
    exceptionHandled.set(false);
    orHandleException(testRunnable(), x -> exceptionHandled.set(true));
    assertThat(exceptionHandled.get()).isFalse();
  }

  @Test
  void orHandleException_throwingFunction_shouldHandleException() {
    AtomicReference<Boolean> exceptionHandled = new AtomicReference<>();
    exceptionHandled.set(false);
    orHandleException(exteptionThrowingRunnable(), x -> exceptionHandled.set(true));
    assertThat(exceptionHandled.get()).isTrue();
  }

  @Test
  void orHandleCheckedException_nonThrowingFunction_shouldNotHandleException() {
    AtomicReference<Boolean> exceptionHandled = new AtomicReference<>();
    exceptionHandled.set(false);
    FunctionResultGuards.orHandleException(testRunnable(), x -> exceptionHandled.set(true));
    assertThat(exceptionHandled.get()).isFalse();
  }

  @Test
  void orHandleCheckedException_throwingFunction_shouldHandleException() {
    AtomicReference<Boolean> exceptionHandled = new AtomicReference<>();
    exceptionHandled.set(false);
    FunctionResultGuards.orHandleException(checkedExceptionThrowingRunnable(), x -> exceptionHandled.set(true));
    assertThat(exceptionHandled.get()).isTrue();
  }

}
