package com.compilit.functions;

import static com.compilit.functions.FunctionGuards.orDefault;
import static com.compilit.functions.FunctionGuards.orDefault;
import static com.compilit.functions.FunctionGuards.orHandleCheckedException;
import static com.compilit.functions.FunctionGuards.orHandleException;
import static com.compilit.functions.FunctionGuards.orNull;
import static com.compilit.functions.FunctionGuards.orNull;
import static com.compilit.functions.FunctionGuards.orRuntimeException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static com.compilit.functions.TestFunctions.checkedExceptionThrowingFunction;
import static com.compilit.functions.TestFunctions.checkedExceptionThrowingSupplier;
import static com.compilit.functions.TestFunctions.runtimeExceptionThrowingFunction;
import static com.compilit.functions.TestFunctions.runtimeExceptionThrowingSupplier;
import static com.compilit.functions.TestFunctions.testFunction;
import static com.compilit.functions.TestFunctions.testSupplier;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

public class FunctionGuardsTests {

  private static final String TEST_VALUE = "test";
  private static final String DEFAULT_TEST_VALUE = "default";

  @Test
  void orNull_noException_shouldReturnValue() {
    assertThat(orNull(testSupplier()).get()).isEqualTo(TEST_VALUE);
  }

  @Test
  void orNull_exception_shouldReturnNull() {
    assertThat(orNull(runtimeExceptionThrowingSupplier()).get()).isNull();
  }

  @Test
  void orNull_checkedException_shouldReturnNull() {
    assertThat(FunctionGuards.orNullThrowing(checkedExceptionThrowingSupplier()).get()).isNull();
  }

  @Test
  void orRuntimeException_checkedException_shouldThrowException() {
    assertThatThrownBy(() -> orRuntimeException(checkedExceptionThrowingSupplier()).get())
      .isInstanceOf(RuntimeException.class);
  }

  @Test
  void orDefault_noException_shouldReturnValue() {
    assertThat(orDefault(testSupplier(), DEFAULT_TEST_VALUE).get()).isEqualTo(TEST_VALUE);
  }

  @Test
  void orDefault_exception_shouldReturnDefaultValue() {
    assertThat(orDefault(runtimeExceptionThrowingSupplier(), DEFAULT_TEST_VALUE).get()).isEqualTo(
      DEFAULT_TEST_VALUE);
  }

  @Test
  void orNull_runtimeExceptionThrowingFunction_shouldReturnNull() {
    var function = orNull(runtimeExceptionThrowingFunction());
    assertThat(function.apply(1)).isNull();
  }

  @Test
  void orNull_nonThrowingFunction_shouldReturnFunction() {
    var function = FunctionGuards.orNullThrowing((ThrowingSupplier<?, ? extends Exception>) () -> String.valueOf(1));
    assertThat(function.get()).isEqualTo("1");
  }

  @Test
  void orNull_throwingFunction_shouldReturnNull() {
    var function = FunctionGuards.orNullThrowing(checkedExceptionThrowingSupplier());
    assertThat(function.get()).isNull();
  }

  @Test
  void orDefault_nonThrowingFunction_shouldReturnFunction() {
    var function = orDefault(testFunction(), "-1");
    assertThat(function.apply(1)).isEqualTo("1");

  }

  @Test
  void orDefault_throwingFunction_shouldReturnDefault() {
    var function = FunctionGuards.orDefaultThrowing(checkedExceptionThrowingFunction(), "-1");
    assertThat(function.apply(1)).isEqualTo("-1");
  }

  @Test
  void orDefault_throwingFunction_shouldReturnNull() {
    var function = FunctionGuards.orDefaultThrowing(checkedExceptionThrowingSupplier(), null);
    assertThat(function.get()).isNull();
  }

  @Test
  void orHandleException_nonThrowingFunction_shouldNotHandleException() {
    AtomicReference<Boolean> exceptionHandled = new AtomicReference<>();
    exceptionHandled.set(false);
    orHandleException(() -> System.out.println(), x -> exceptionHandled.set(true)).run();
    assertThat(exceptionHandled.get()).isFalse();
  }

  @Test
  void orHandleException_throwingFunction_shouldHandleException() {
    AtomicReference<Boolean> exceptionHandled = new AtomicReference<>();
    exceptionHandled.set(false);
    orHandleException(() -> {throw new RuntimeException();}, x -> exceptionHandled.set(true)).run();
    assertThat(exceptionHandled.get()).isTrue();
  }

  @Test
  void orHandleCheckedException_nonThrowingFunction_shouldNotHandleException() {
    AtomicReference<Boolean> exceptionHandled = new AtomicReference<>();
    exceptionHandled.set(false);
    orHandleCheckedException(() -> System.out.println(), x -> exceptionHandled.set(true)).run();
    assertThat(exceptionHandled.get()).isFalse();
  }

  @Test
  void orHandleCheckedException_throwingFunction_shouldHandleException() {
    AtomicReference<Boolean> exceptionHandled = new AtomicReference<>();
    exceptionHandled.set(false);
    orHandleCheckedException(() -> {throw new Exception();}, x -> exceptionHandled.set(true)).run();
    assertThat(exceptionHandled.get()).isTrue();
  }

}
