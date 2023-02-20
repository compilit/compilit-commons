package com.compilit.functions;

import java.util.function.Function;
import java.util.function.Supplier;
import com.compilit.functions.api.ThrowingFunction;
import com.compilit.functions.api.ThrowingRunnable;
import com.compilit.functions.api.ThrowingSupplier;

class TestFunctions {
  static final String TEST_VALUE = "test";
  static final String DEFAULT_TEST_VALUE = "default";
  static Supplier<String> testSupplier() {
    return () -> TEST_VALUE;
  }

  static <E extends Exception> ThrowingSupplier<String, E> testThrowingSupplier() {
    return () -> TEST_VALUE;
  }

  static <T> Supplier<T> runtimeExceptionThrowingSupplier() {
    return () -> {throw new RuntimeException();};
  }

  static <I,O> Function<I, O> runtimeExceptionThrowingFunction() {
    return x -> {throw new RuntimeException();};
  }

  static <E extends Exception> ThrowingSupplier<String, E> checkedExceptionThrowingSupplier() {
    return () -> {throw new Exception();};
  }

  static <I, O, E extends Exception> ThrowingFunction<I, O, E> checkedExceptionThrowingFunction() {
    return x -> {throw new Exception();};
  }

  static <E extends Exception> ThrowingRunnable<E> checkedExceptionThrowingRunnable() {
    return () -> {throw new Exception();};
  }

  static <I> Function<I, String> testFunction() {
    return Object::toString;
  }
  static Runnable testRunnable() {
    return () -> System.out.println("Random");
  }

  static Runnable exteptionThrowingRunnable() {
   return () -> {throw new RuntimeException();};
  }
}
