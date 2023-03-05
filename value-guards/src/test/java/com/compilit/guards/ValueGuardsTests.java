package com.compilit.guards;

import static com.compilit.guards.TestFunctions.DEFAULT_TEST_VALUE;
import static com.compilit.guards.TestFunctions.TEST_VALUE;
import static com.compilit.guards.TestFunctions.exceptionThrowingFunction;
import static com.compilit.guards.TestFunctions.exceptionThrowingSupplier;
import static com.compilit.guards.TestFunctions.exceptionThrowingThrowingFunction;
import static com.compilit.guards.TestFunctions.exceptionThrowingThrowingRunnable;
import static com.compilit.guards.TestFunctions.exceptionThrowingThrowingSupplier;
import static com.compilit.guards.TestFunctions.exteptionThrowingRunnable;
import static com.compilit.guards.TestFunctions.testFunction;
import static com.compilit.guards.TestFunctions.testRunnable;
import static com.compilit.guards.TestFunctions.testSupplier;
import static com.compilit.guards.ValueGuards.orDefault;
import static com.compilit.guards.ValueGuards.orHandleException;
import static com.compilit.guards.ValueGuards.orNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

class ValueGuardsTests {

  @Test
  void orNull_noException_shouldReturnValue() {
    assertThat(orNull(testSupplier())).isEqualTo(TEST_VALUE);
  }

  @Test
  void orNull_exception_shouldReturnNull() {
    var actual = orNull(exceptionThrowingSupplier());
    assertThat(actual).isNull();
  }

  @Test
  void orNull_checkedException_shouldReturnNull() {
    assertThat(ValueGuards.orNullThrowing(exceptionThrowingThrowingSupplier())).isNull();
  }

  @Test
  void orDefault_noException_shouldReturnValue() {
    assertThat(orDefault(testSupplier(), DEFAULT_TEST_VALUE)).isEqualTo(TEST_VALUE);
  }

  @Test
  void orDefault_exception_shouldReturnDefaultValue() {
    assertThat(orDefault(exceptionThrowingSupplier(), DEFAULT_TEST_VALUE)).isEqualTo(DEFAULT_TEST_VALUE);
  }

  @Test
  void orNull_nonThrowingFunction_shouldReturnResult() {
    var result = orNull(testFunction(), 1);
    assertThat(result).isEqualTo("1");
  }

  @Test
  void orNull_throwingFunction_shouldReturnNull() {
    var result = orNull(exceptionThrowingFunction(), 1);
    assertThat(result).isNull();
  }

  @Test
  void orNull_throwingSupplier_shouldReturnNull() {
    var result = ValueGuards.orNullThrowing(exceptionThrowingThrowingSupplier());
    assertThat(result).isNull();
  }

  @Test
  void orDefault_nonThrowingFunction_shouldReturnResult() {
    assertThat(orDefault(testFunction(), 1, "-1")).isEqualTo("1");

  }

  @Test
  void orDefault_throwingFunction_shouldReturnDefault() {
    assertThat(ValueGuards.orDefaultThrowing(exceptionThrowingThrowingFunction(), 1, "-1")).isEqualTo("-1");
  }

  @Test
  void orDefault_throwingFunction_shouldReturnNull() {
    var result = ValueGuards.orDefaultThrowing(exceptionThrowingThrowingSupplier(), null);
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
    ValueGuards.orHandleException(testRunnable(), x -> exceptionHandled.set(true));
    assertThat(exceptionHandled.get()).isFalse();
  }

  @Test
  void orHandleCheckedException_throwingFunction_shouldHandleException() {
    AtomicReference<Boolean> exceptionHandled = new AtomicReference<>();
    exceptionHandled.set(false);
    ValueGuards.orHandleException(exceptionThrowingThrowingRunnable(), x -> exceptionHandled.set(true));
    assertThat(exceptionHandled.get()).isTrue();
  }

}
